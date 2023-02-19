/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.core.plugin;

import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Plugin - OAuth2插件
 * 
 * @author rsico Team
 * @version 3.0
 */
public abstract class AuthPlugin  implements Comparable<AuthPlugin> {

	public static String SESSION_KEY="oauth2-session-{0}";

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public final String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 *
	 * @return 版本
	 */
	public abstract String getVersion();

	public abstract List<PluginAttribute> getAttributeKeys();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		AuthPlugin other = (AuthPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public int compareTo(AuthPlugin storagePlugin) {
		return new CompareToBuilder().append(getId(), storagePlugin.getId()).toComparison();
	}

	/**
	 * 获取用户令牌
	 * 返回 {token:,userId,}
	 */
	public abstract Map<String,String> getOauth2AccessToken(SysPlugin sysPlugin,String auth_code) throws Exception;

	/**
	 * 获取用户信息
	 * @return {"return_code", "0000","nickname","unionid","userid","avatar","mobile","error_msg"};
	 */
	public abstract Map<String,String> getUserInfoByToken(SysPlugin sysPlugin,String token,String userId,String encryptData,String iv,String sessionKey) throws Exception;

	/**
	 * POST请求
	 * @param url URL
	 * @param data 请求参数
	 * @return 返回结果
	 */
	public String post(String url,String data) {
		Assert.hasText(url);
		String result = null;

		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(new StringEntity(data, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
	public String get(String url, Map<String, Object> parameterMap) {
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
					if (StringUtils.isNotEmpty(name)) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
}