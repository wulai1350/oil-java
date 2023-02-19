package com.rzico.core.security;

import com.alibaba.fastjson.JSONObject;
import com.rzico.base.CommResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * @author Rzico Boot
 * @date 2019-12-07
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("type", CommResult.Type.logout);
        jsonObject.put("content", "令牌无效");
        response.getWriter().println(jsonObject.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
