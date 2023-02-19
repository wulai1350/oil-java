package com.rzico.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

/**
 * Created by zhangsr on 2020/6/20.
 */
@Slf4j
public class HttpUtils {

    public static TreeMap<String, Object> requestParam(HttpServletRequest httpServletRequest){
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        Map reqMap = httpServletRequest.getParameterMap();
        for(Object key:reqMap.keySet()){
            String value = ((String[])reqMap.get(key))[0];
            if (com.rzico.util.StringUtils.isNotEmpty(value)){
                map.put(key.toString(),value);
            }
        }
        return map;
    }

    /**
     * POST请求
     * @param url URL
     * @param data 请求参数
     * @return 返回结果
     */
    public static String httpPost(String url,String data,HttpClient httpClient) {
        Assert.hasText(url);
        String result = null;
        if (httpClient==null) {
            httpClient = new DefaultHttpClient();
        }
        try {
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * GET请求
     * @param url URL
     * @param parameterMap 请求参数
     * @return 返回结果
     */
    public static String httpGet(String url, Map<String, Object> parameterMap) {
        Assert.hasText(url);
        String result = null;
        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (parameterMap != null) {
                for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    String value = ConvertUtils.convert(entry.getValue());
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(name)) {
                        nameValuePairs.add(new BasicNameValuePair(name, value));
                    }
                }
            }
            HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 执行HttpPost请求
     * @param url 请求路径
     * @param params 参数
     * @return 结果
     */
    public static String httpPost(String url, Map<String, Object> params){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost =   new HttpPost(url);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (null != params && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (key == null || value == null) {
                        continue;
                    }
                    NameValuePair pair = new BasicNameValuePair(key, value);
                    nameValuePairs.add(pair);
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            // 执行请求
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                log.info("执行请求完毕：" + result);
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("请求通信[\" + reqURL + \"]时网络时，关闭异常,堆栈轨迹如下", e);
            }
            httpPost.releaseConnection();
        }
        return result;
    }

}
