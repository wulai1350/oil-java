/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页
 * 
 * @author rsico Team
 * @version 3.0
 */
@Data
public class PageResult<T> implements Serializable {

	/** 内容 */
	private List<T> data = new ArrayList<T>();

	/** 总记录数 */
	private final long total;

	/** 分页信息 */
	private final Pageable pageable;

	/**
	 * 初始化一个新创建的Page对象
	 */
	public PageResult() {
		this.total = 0L;
		this.pageable = new Pageable();
	}

	/**
	 * @param content
	 *            内容
	 * @param total
	 *            总记录数
	 * @param pageable
	 *            分页信息
	 */
	public PageResult(List<T> content, long total, Pageable pageable) {
		this.data = content;
		this.total = total;
		this.pageable = pageable;
	}

	public Long getTotal() {
		return this.total;
	}

	@JsonIgnore
	public int getPageSize() {
		return this.pageable.getPageSize();
	}

	/**
	 * 获取总页数
	 * 
	 * @return 总页数
	 */

	@JsonIgnore
	public int getTotalPages() {
		return (int) Math.ceil((double) getTotal() / (double) getPageSize());
	}

}