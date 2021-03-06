package com.yang.dao.mapper;

import com.yang.dao.entry.City;

import java.util.List;

/**
 * @Description: 一级缓存的mapper
 * @Author: tona.sun
 * @Date: 2020/05/18 17:47
 */
public interface CityMapper {
    List<City> selectAll();
}
