package com.rzico.core.aspect;

import com.alibaba.fastjson.JSON;

import com.rzico.annotation.Log;
import com.rzico.core.entity.SysLog;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysLogService;
import com.rzico.core.service.SysUserService;
import com.rzico.util.CodeGenerator;
import com.rzico.util.IPUtils;
import com.rzico.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author zhuxiaomeng
 * @date 2017/12/28.
 * @email 154040976@qq.com
 * <p>
 * 为增删改添加监控
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysUserService sysUserService;

    @Pointcut("@annotation(com.rzico.annotation.Log)")
    private void pointcut() {

    }

    @After("pointcut()")
    public void insertLogSuccess(JoinPoint jp) {
        addLog(jp, getDesc(jp));
    }

    private void addLog(JoinPoint jp, String text) {
        Log.LOG_TYPE type = getType(jp);
        SysLog log = new SysLog();
        log.setId(CodeGenerator.getUUID());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //一些系统监控
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IPUtils.getIp(request);
            String requestUri = request.getRequestURI();
            String requestUrl = request.getRequestURL().toString();
            log.setRemoteAddr(ip);
            log.setRequestUri(requestUri);
            log.setServerAddr(requestUrl);
            if (StringUtils.isNotEmpty(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer")){
                SysUser sysUser = sysUserService.getCurrent();
                if (null != sysUser){
                    log.setUsername(sysUser.getUsername());
                    log.setMchId(sysUser.getMchId());
                }
            }
        }
        log.setLogType(type.toString());
        log.setLogTitle(text);
        Object[] obj = jp.getArgs();
        StringBuffer buffer = new StringBuffer();
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                buffer.append("[参数" + (i + 1) + ":");
                Object o = obj[i];
                if(o instanceof Model){
                    continue;
                }
                String parameter=null;
                try {
                    parameter=JSON.toJSONString(o);
                }catch (Exception e){
                    continue;
                }
                buffer.append(parameter);
                buffer.append("]");
            }
        }
        log.setRequestParams(buffer.toString());

        sysLogService.insert(log);
    }

    /**
     * 记录异常
     *
     * @param joinPoint
     * @param
     */
  /*  @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterException(JoinPoint joinPoint, Exception e) {
        System.out.print("-----------afterException:" + e.getMessage());
        addLog(joinPoint, getDesc(joinPoint) + e.getMessage());
    }*/


    private String getDesc(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).desc();
    }

    private Log.LOG_TYPE getType(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).type();
    }

}

