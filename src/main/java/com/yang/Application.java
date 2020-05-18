package com.yang;

import com.yang.dao.entry.City;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description: TODO
 * @Author: tona.sun
 * @Date: 2020/05/18 17:33
 */
public class Application {
    public static void main(String[] args) throws IOException {
        LogFactory.useLog4JLogging();
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //两次查询只打印一条sql，mybatis一级缓存，只执行一次sql
            List<City> cities = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities.size());
            List<City> cities2 = session.selectList("com.yang.dao.mapper.CityMapper.selectAll");
            System.out.println(cities2.size());
        }
    }
}
