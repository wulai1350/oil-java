/**
 * 版权所有 违者必究
 * by chenxh
 */
package com.rzico.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AreaUtils {


    public static Map<String,Object>  encodeMap(Long areaId,Map<String,Object> params) {
        String areaStr = String.valueOf(areaId);
        if (areaStr.endsWith("00")) {
            areaStr = areaStr.substring(0,areaStr.length()-2);
        }
        if (areaStr.endsWith("00")) {
            areaStr = areaStr.substring(0,areaStr.length()-2);
        }
        String beginStr = areaStr;
        String endStr = areaStr;
        for (int i=0;i<(6-beginStr.length());i++) {
            beginStr = beginStr+"0";
        }
        for (int i=0;i<(6-endStr.length());i++) {
            endStr = endStr+"9";
        }

        params.put("beginArea",Long.valueOf(beginStr));
        params.put("endArea",Long.valueOf(endStr));

        return params;
    }


    public static void main(String[] args) throws Throwable {

    }

}