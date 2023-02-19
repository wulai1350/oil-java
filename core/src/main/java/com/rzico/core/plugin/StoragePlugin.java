/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.core.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Plugin - 存储
 * 
 * @author rsico Team
 * @version 3.0
 */
public abstract class StoragePlugin implements Comparable<StoragePlugin> {

	public static String imageType=".jpg,.bmp,.jpe,.jpeg,.png,.gif";
	public static String videoType=".mp4,.mkv,.flv,.swf,.avi,.rm,.rmvb,.mpeg,.mpg";
	public static String musicType=".mp3,.wav";

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
	/**
	 * 获取请求参数
	 * @param path 保存路径
	 * @param file 文件
	 * @param sysPlugin 插件
	 * @param contentType 文件类型
	 * @return
	 */
	public abstract void upload(SysPlugin sysPlugin,String path, MultipartFile file, String contentType) throws IOException;

	public String getMineType(String name) {
		Map<String ,String> data = new HashMap<String,String>();
		data.put(".jpg","image/jpeg");
		data.put(".jpe","image/jpeg");
		data.put(".jpeg","image/jpeg");
		data.put(".bmp","image/bmp");
		data.put(".png","image/png");
		data.put(".gif","image/gif");
		data.put(".mp4","video/mp4");
		data.put(".mkv","video/x-m4v");
		data.put(".flv","video/x-flv");
		data.put(".swf","application/x-shockwave-flash");
		data.put(".avi","video/x-msvideo");
		data.put(".rm","application/vnd.rn-realmedia");
		data.put(".rmvb","application/vnd.rn-realmedia");
		data.put(".mpeg","video/mpeg");
		data.put(".mpg","video/mpeg");
		data.put(".mp3","audio/mpeg");
		data.put(".wav","audio/wav");
		if (data.containsKey(name.toLowerCase()) ) {
			return data.get(name.toLowerCase());
		} else {
			return "application/octet-stream";
		}
	}

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
		StoragePlugin other = (StoragePlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public int compareTo(StoragePlugin storagePlugin) {
		return new CompareToBuilder().append(getId(), storagePlugin.getId()).toComparison();
	}

	public String getSuffixByFilename(String filename) {
		return filename.substring(filename.lastIndexOf(".")).toLowerCase();
	}

	public Integer getFileType(String suffix) {
		if (imageType.indexOf(suffix.toLowerCase())>=0) {
	       return 0;
		} else
		if (videoType.indexOf(suffix.toLowerCase())>=0) {
			return 1;
		} else
		if (musicType.indexOf(suffix.toLowerCase())>=0) {
			return 2;
		} else {
			return 3;
		}
	}

}