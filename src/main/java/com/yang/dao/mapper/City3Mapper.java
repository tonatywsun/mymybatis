package com.yang.dao.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 绕过二级缓存更新数据的Mapper
 * @Author: tona.sun
 * @Date: 2020/05/19 11:30
 */
public interface City3Mapper {
    Integer updateCity(@Param("name") String name);
}
