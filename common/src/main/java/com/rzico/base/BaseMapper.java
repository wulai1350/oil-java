package com.rzico.base;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface BaseMapper<T, E extends Serializable> extends tk.mybatis.mapper.common.BaseMapper<T>, MySqlMapper<T>, IdsMapper<T>, ConditionMapper<T>,ExampleMapper<T> {


    public List<T> selectList(Map<String, Object> params);
    public int selectRowCount(Map<String, Object> params);

}
