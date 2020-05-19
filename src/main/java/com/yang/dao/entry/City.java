package com.yang.dao.entry;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @Description: 二级缓存要实现Serializable
 * @Author: tona.sun
 * @Date: 2020/05/18 17:43
 */
public class City implements Serializable {
    private static final long serialVersionUID = 6695323450847736493L;
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
