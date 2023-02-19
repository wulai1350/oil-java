/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.core.plugin;

import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin - 存储
 * 
 * @author rsico Team
 * @version 3.0
 */
public abstract class MsgPlugin implements Comparable<MsgPlugin> {

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

	public abstract void sendMsg(SysPlugin sysPlugin,String username,String content) throws IOException;

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
		MsgPlugin other = (MsgPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	@Override
	public int compareTo(MsgPlugin msgPlugin) {
		return new CompareToBuilder().append(getId(), msgPlugin.getId()).toComparison();
	}


}