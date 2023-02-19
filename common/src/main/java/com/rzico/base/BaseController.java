package com.rzico.base;

import com.alibaba.fastjson.JSON;
import com.rzico.entity.Pageable;
import com.rzico.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public abstract class BaseController<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public String json2Str(Object object){
        return JSON.toJSONString(object);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");
    }

    protected Map<String, Object> buildSortField( Map<String, Object> params, Pageable pageable){
        String sortField = pageable.getSortField();
        if (StringUtils.isNotEmpty(sortField)){
            params.put("sortField", StringUtils.propertyToField(sortField));
        }else{
            params.put("sortField", "id");
        }
        params.put("sortType", pageable.getDirection()==null?"DESC":pageable.getDirection().name());
        return params;
    }

}
