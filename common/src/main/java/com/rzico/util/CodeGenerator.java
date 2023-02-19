package com.rzico.util;

import java.util.UUID;

/**
 * Created by zhongzm on 2018/12/22.
 */
public class CodeGenerator {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (uuid.startsWith("89") || uuid.startsWith("88")) {
            uuid = "A"+uuid.substring(1);
        }
        return uuid.toUpperCase();
    }
}
