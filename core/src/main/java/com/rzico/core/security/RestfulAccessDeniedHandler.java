package com.rzico.core.security;

import com.alibaba.fastjson.JSONObject;
import com.rzico.base.CommResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当访问接口没有权限时，自定义的返回结果
 * @author Rzico Boot
 * @date 2019-12-07
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("type", CommResult.Type.error);
        jsonObject.put("content", "无访问权限");
        response.getWriter().println(jsonObject.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
