package com.rzico.annotation;

import java.lang.annotation.*;

/**
 * @author zhongzm
 * 记录日志
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Log {

    public enum LOG_TYPE {
        ADD,
        UPDATE,
        DEL,
        SELECT,
        LOGIN,
        /**
         * 身份验证
         */
        ATHOR
    }

    /**
     * 内容
     */
    String desc();

    /**
     * 类型 curd
     */
    LOG_TYPE type() default LOG_TYPE.ATHOR;

}
