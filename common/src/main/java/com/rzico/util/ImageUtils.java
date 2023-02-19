package com.rzico.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author zhuxiaomeng
 * @date 2017/12/28.
 * @email 154040976@qq.com
 */
public class ImageUtils {

    public static String cropImage(String url,Integer w,Integer h) {

        if (url.indexOf("?x-oss-") != -1) {
            url = url.substring(0, url.indexOf("?x-oss-"));
        }
        h = Math.round(h);
        w = Math.round(w);
        if (url.substring(0, 11) == "http://cdnx") {
            return url + "?x-oss-process=image/resize,m_fill,w_" + String.valueOf(w) + ",h_" + String.valueOf(h) + "/quality,q_90";
        } else if (url.substring(0, 10) == "http://cdn") {
            return url + "@" + String.valueOf(w) + "w_" + String.valueOf(h) + "h_1e_1c_100Q";
        } else {
            return url;
        }
    }


}
