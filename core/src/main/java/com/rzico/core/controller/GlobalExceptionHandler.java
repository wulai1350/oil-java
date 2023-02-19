package com.rzico.core.controller;


import com.rzico.base.CommResult;
import com.rzico.exception.CustomException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 注解@ControllerAdvice作用域是全局Controller范围，即必须与抛出异常的method在同一个controller
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-02-21
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局异常捕捉处理
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommResult jsonErrorHandler(HttpServletRequest req,
                                       Exception e) throws Exception {
        String errorMsg = e.getMessage();
        logger.error(ExceptionUtils.getStackTrace(e));
        return new CommResult().error(errorMsg);
    }

    /**
     * 断言异常处理
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseBody
    public CommResult assertErrorHandler(HttpServletRequest req,
                                         Exception e) throws Exception {
        String errorMsg = e.getMessage();
        logger.error(ExceptionUtils.getStackTrace(e));
        return  new CommResult().error(errorMsg);
    }


    /**
     * 自定义异常处理
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public CommResult customErrorHandler(HttpServletRequest req,
                                         Exception e) throws Exception {
        String errorMsg = e.getMessage();
        logger.error(ExceptionUtils.getStackTrace(e));
        return  new CommResult().error(errorMsg);
    }
}
