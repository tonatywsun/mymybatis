package com.yang;

import com.yang.dao.entry.City;
import com.yang.dao.mapper.City2Mapper;
import com.yang.dao.mapper.City3Mapper;
import com.yang.dao.mapper.CityMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description: 纯mybatis 没有集成spring
 * @Author: tona.sun
 * @Date: 2020/05/18 17:33
 */
public class Application {
    public static void main(String[] args) throws IOException {
        LogFactory.useLog4JLogging();
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //一级缓存
        //源码 org.apache.ibatis.executor.BaseExecutor.query-->list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //四次查询只打印一条sql，mybatis一级缓存，只执行一次sql
            CityMapper mapper = session.getMapper(CityMapper.class);
            List<City> cities1 = mapper.selectAll();
            System.out.println(cities1.size());
            List<City> cities2 = mapper.selectAll();
            System.out.println(cities2.size());
            List<City> cities3 = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities3.size());
            List<City> cities4 = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities4.size());
        }
        //以下情况不会一级缓存
        //不同的sqlsession
        //sqlSession相同，查询条件不同。因为缓存条件不同，缓存中还没有数据。
        //sqlSession相同，在两次相同查询条件中间执行过增删改操作
        //sqlSession相同，手动清空了一级缓存。
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //两次查询只打印一条sql，mybatis一级缓存，只执行一次sql，但是没有用到上面的一级缓存
            List<City> cities = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities.size());
            //手动清空了一级缓存
            session.clearCache();
            List<City> cities2 = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities2.size());
        }
        System.out.println("------------------------------");
        //二级缓存
        //Map<String, MappedStatement> mappedStatements中key是com.yang.dao.mapper.CityMapper.selectAll对于同一个key对应mappedStatements是相同
        //二级缓冲private Cache cache;是MappedStatement的一个属性
        //源码org.apache.ibatis.executor.CachingExecutor.query方法中-->Cache cache = ms.getCache();
        //下面两句查询虽然不是同一个sqlsession但是开启了二级缓存只执行了一条sql
        try (SqlSession session = sqlSessionFactory.openSession()) {
            City2Mapper mapper = session.getMapper(City2Mapper.class);
            List<City> cities1 = mapper.selectAll();
            System.out.println(cities1);
        }
        //此处换了一个mapper修改了数据库中的数据，但是二级缓存没有修改，因为二级缓存跟namespace有关，这里换了City3Mapper跟City2Mapper的二级缓存没有关联了
        //所以若果在不同的mapper中操作同一个表开启了二级缓存会发生bug
        try (SqlSession session = sqlSessionFactory.openSession()) {
            City3Mapper mapper = session.getMapper(City3Mapper.class);
            mapper.updateCity("1111");
            session.commit();
        }
        try (SqlSession session = sqlSessionFactory.openSession()) {
            City2Mapper mapper = session.getMapper(City2Mapper.class);
            List<City> cities1 = mapper.selectAll();
            System.out.println(cities1);
        }
    }
}
