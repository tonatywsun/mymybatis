package com.yang.dao.mapper;

import com.yang.dao.entry.City;

import java.util.List;

/**
 * @Description: 开启二级缓存的Mapper
 * @Author: tona.sun
 * @Date: 2020/05/19 11:30
 */
public interface City2Mapper {
    List<City> selectAll();
}
