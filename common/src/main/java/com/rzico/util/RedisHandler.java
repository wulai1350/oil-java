/**
 * 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
 * 日    期：2020-01-16
 */
package com.rzico.util;

import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisHandler
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-02-06
 */
public class RedisHandler {
    /**
     * redis key前缀
     */
    public static final String CORE_MODULE_YZM_KEY = "CORE:YZM:{0}";

    //验证码有效期(单位秒)
    public static final Long YZM_EXPIRED_TIME = 600L;

    public static final Long CACHE_EXPIRED_TIME_TWO_HOURS = 7200L;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * value 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String,Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * value 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String,Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * value 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }


    /**
     * 哈希 添加
     */
    public void setHash(String key, Map map){
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key,map);
    }
    /**
     * 哈希 添加
     */
    public void setHash(String key, Map map, Long expireTime){
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key,map);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    public Map getHash(String key){

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }

    public Boolean lock(String key,String value) {
        try {
            ValueOperations<String,Object> operations = redisTemplate.opsForValue();
            return operations.setIfAbsent(key, value,20,TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void unlock(String key,String value) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        if (value.equals(operations.get(key))) {
            redisTemplate.delete(key);
        };

    }


}
