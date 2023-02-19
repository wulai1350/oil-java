package com.rzico.base.impl;

import com.rzico.base.BaseMapper;
import com.rzico.base.BaseService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.jwt.JwtTokenUtil;
import com.rzico.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public abstract class BaseServiceImpl<T, E extends Serializable> implements BaseService<T, E> {


    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * general field(通用字段)
     */

    private static final String CREATE_BY = "createBy";

    private static final String CREATE_DATE = "createDate";

    private static final String MODIFY_BY = "modifyBy";

    private static final String MODIFY_DATE = "modifyDate";

    //系统默认 id 如果主键为其他字段 则需要自己手动 生成 写入 id
    private static final String ID = "id";

    private static final String STR = "java.lang.String";
    private static final String LONG = "java.lang.Long";


    public abstract BaseMapper<T, E> getMapper();

    public String getAuthorization() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String authToken = request.getHeader("Authorization");
                if (authToken==null) {
                    authToken = request.getParameter("token");
                }
                return authToken;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String,String> getToken() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String authToken = request.getHeader("Authorization");
                String mchId = request.getHeader("mchId");
                if (authToken==null) {
                    authToken = request.getParameter("token");
                }
                if (authToken != null) {
                    //判断令牌是否过期
                    if (!jwtTokenUtil.isTokenExpired(authToken)) {
                        Map<String,String> userInfo = jwtTokenUtil.getUserInfoFromToken(authToken);
                        if (mchId!=null) {
                            userInfo.put("hMchId", mchId);
                        }
                        return userInfo;
                    }
                };
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<T> select(T t) {
        return getMapper().select(t);
    }

    @Override
    public T selectOne(T t) {
        return getMapper().selectOne(t);
    }

    @Override
    public int count(T t) {
        return getMapper().selectCount(t);
    }

    /**
     * 可执行级联查询
     * @param o
     * @return
     */
    @Override
    public T selectByPrimaryKey(Object o) {
        return getMapper().selectByPrimaryKey(o);
    }

    @Override
    public T findById(Object o) {
        Map<String,Object> params = new HashMap<>();
        params.put("id",o);
        List<T> list = selectList(params);
        if (list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 可执行级联查询
     * @param params
     * @return
     */
    @Override
    public List<T> selectList(Map<String, Object> params){
        return getMapper().selectList(params);
    }

    /**
     * 统计记录数
     * @param params
     * @return
     */
    @Override
    public int selectRowCount(Map<String, Object> params){
        return getMapper().selectRowCount(params);
    }

    /**
     * 不支持级联查询
     * @param pageable
     * @return
     */
    @Override
    public List<T> selectByExample(Class<T> clazz,Pageable pageable){
        Example example = new Example(clazz);
        criteriaBuilder(example, pageable);
        return getMapper().selectByExample(example);
    }


    /**
     * 不支持级联查询
     * @param params
     * @return
     */
    @Override
    public List<T> selectByExample(Class<T> clazz,Map<String, Object> params){
        Example example = new Example(clazz);
        criteriaBuilderMap(example, params);
        return getMapper().selectByExample(example);
    }

    @Override
    public int insert(T record) {
        try {
            record = addValue(record, true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getMapper().insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertList(List<T> list) {
        int  rw = 0;
        for (T record:list) {
            rw = rw +insert(record);
        }
        return rw;
    }

    @Override
    public int insertUseGeneratedKeys(T t) {
        addValue(t,true);
        return getMapper().insertUseGeneratedKeys(t);
    }

    @Override
    public int insertUseGeneratedKeyList(List<T> list) {
        for (T record:list) {
            addValue(record,true);
        }
        return getMapper().insertList(list);
    }

    @Override
    public boolean existsWithPrimaryKey(Object o) {
        return getMapper().existsWithPrimaryKey(o);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object o:ids) {
            rw = rw +deleteByPrimaryKey(o);
        }
        return rw;
    }
    @Override
    public int deleteByPrimaryKey(Object id) {
        return getMapper().deleteByPrimaryKey(id);
    }


    @Override
    public int delete(T t) {
        return getMapper().delete(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        addValue(record, false);
        return getMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        addValue(record, false);
        return getMapper().updateByPrimaryKey(record);
    }

    /**
     * 通用注入创建 更新信息 可通过super调用
     *
     * @param record
     * @param flag
     * @return
     */
    public T addValue(T record, boolean flag) {
        //统一处理公共字段
        Class<?> clazz = record.getClass();
        String operateDate;
        try {
            if (flag) {
                Map<String,String> currentUser = getToken();

                //软件删除表 支持 delFlag 或 deleted
                try {
                    //添加 id uuid支持
                    Field idField = clazz.getDeclaredField(ID);
                    if (idField!=null) {
                        idField.setAccessible(true);
                        Object o = idField.get(record);
                        Class<?> type = idField.getType();
                        String name = type.getName();
                        if ((o == null) && STR.equals(name)) {
                            //已经有值的情况下 不覆盖
                            idField.set(record, UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        }
                    }

                    Field delField = clazz.getDeclaredField("delFlag");
                    if (delField==null) {
                        delField = clazz.getDeclaredField("deleted");
                    }
                    if (delField!=null) {
                        delField.set(record,false);
                    }
                } catch (Exception e){
                }

                operateDate = CREATE_DATE;
            } else {
                operateDate = MODIFY_DATE;
            }
            try {
                Field fieldDate = clazz.getDeclaredField(operateDate);
                if (fieldDate!=null) {
                    fieldDate.setAccessible(true);
                    fieldDate.set(record, new Date());
                }
            } catch (Exception e){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }

    public Example.Criteria criteriaBuilder(Example example, Pageable pageable){
        Example.Criteria criteria = example.createCriteria();
        if (pageable.getFilters() != null) {
            for (Filter filter : pageable.getFilters()) {
                if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
                    continue;
                }
                if (filter.getOperator() == Filter.Operator.eq && filter.getValue() != null) {
                    if (null != filter.getValue() && !"".equals(filter.getValue())) {
                        criteria.andEqualTo(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.ne && filter.getValue() != null) {
                    if (null != filter.getValue() && !"".equals(filter.getValue())) {
                        criteria.andNotEqualTo(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.gt && filter.getValue() != null) {
                    if (filter.getValue().getClass().equals(Date.class)) {
                        criteria.andGreaterThan(filter.getProperty(), filter.getValue());
                    }else {
                        criteria.andGreaterThan(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.lt && filter.getValue() != null) {
                    if (filter.getValue().getClass().equals(Date.class)) {
                        criteria.andLessThan(filter.getProperty(), filter.getValue());
                    }else{
                        criteria.andLessThan(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.ge && filter.getValue() != null) {
                    if (filter.getValue().getClass().equals(Date.class)) {
                        criteria.andGreaterThanOrEqualTo(filter.getProperty(), filter.getValue());
                    }else{
                        criteria.andGreaterThanOrEqualTo(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.le && filter.getValue() != null) {
                    if (filter.getValue().getClass().equals(Date.class)) {
                        criteria.andLessThanOrEqualTo(filter.getProperty(), filter.getValue());
                    }else{
                        criteria.andLessThanOrEqualTo(filter.getProperty(), filter.getValue());
                    }
                } else if (filter.getOperator() == Filter.Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
                    criteria.andLike(filter.getProperty(), (String) "%"+filter.getValue()+"%");
                } else if (filter.getOperator() == Filter.Operator.in && filter.getValue() != null) {
                    criteria.andNotIn(filter.getProperty(), (ArrayList)filter.getValue());
                } else if (filter.getOperator() == Filter.Operator.isNull) {
                    criteria.andIsNull(filter.getProperty());
                } else if (filter.getOperator() == Filter.Operator.isNotNull) {
                    criteria.andIsNotNull(filter.getProperty());
                }
            }
            if (StringUtils.isNotEmpty(pageable.getSortField())){
                if (null != pageable.getDirection() && pageable.getDirection().equals(Pageable.Direction.desc)){
                    example.orderBy(pageable.getSortField()).desc();
                }else if (null != pageable.getDirection() && pageable.getDirection().equals(Pageable.Direction.asc)){
                    example.orderBy(pageable.getSortField()).asc();
                }else {
                    example.orderBy(pageable.getSortField()).asc();
                }

            }else{
                example.orderBy("createDate").desc();
            }
        }
        return criteria;
    }


    public Example.Criteria criteriaBuilderMap(Example example, Map<String, Object> params) {
        Example.Criteria criteria = example.createCriteria();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        String sortType = "";
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
            // it.remove(); 删除元素
            if (StringUtils.isEmpty(entry.getKey()) || null == entry.getValue()) {
                continue;
            }

            if (entry.getKey().equals("sortType")) {
                sortType = entry.getValue().toString();
            } else if (entry.getKey().equals("sortField")) {
                if (sortType.equalsIgnoreCase("desc")) {
                    example.orderBy(StringUtils.fieldToProperty(entry.getValue().toString())).desc();
                } else {
                    example.orderBy(StringUtils.fieldToProperty(entry.getValue().toString())).asc();
                }

            }else if (entry.getKey().equals("treePath")){
                criteria.andLike(entry.getKey(), (String) "%"+entry.getValue()+"%");
            }else {
                criteria.andEqualTo(entry.getKey(), entry.getValue());
            }

        }
        return criteria;
    }
}
