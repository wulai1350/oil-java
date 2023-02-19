package com.rzico.base;

import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
   <pre>
 * 通用service层
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
public interface BaseService<T, E extends Serializable> {

    //#
    public Map<String,String> getToken();

    public List<T> select(T t);

    public T selectOne(T t);

    public T selectByPrimaryKey(Object o);

    //#
    public T findById(Object o);

    //#
    public List<T> selectList(Map<String, Object> map);
    //#
    public int selectRowCount(Map<String, Object> map);

    public List<T> selectByExample(Class<T> clazz,Pageable pageable);
    public List<T> selectByExample(Class<T> clazz,Map<String, Object> map);

    public int count(T t);

    public int insert(T record);

    public int insertList(List<T> list);

    public int insertUseGeneratedKeys(T t);
    public int insertUseGeneratedKeyList(List<T> list);

    public int updateByPrimaryKeySelective(T record);

    public int updateByPrimaryKey(T record);

    //#
    public int deleteByIds(Object [] ids);
    public int deleteByPrimaryKey(Object id);

    public int delete(T t);

    public boolean existsWithPrimaryKey(Object o);

}
