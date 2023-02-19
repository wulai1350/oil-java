/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 * 
 * @author rsico Team
 * @version 3.0
 */
@Data
@ApiModel(description= "分页信息")
public class Pageable implements Serializable {
	/**
	 * 方向
	 */
	public enum Direction {

		/** 递增 */
		asc,

		/** 递减 */
		desc
	}

	/** 默认页码 */
	private static final int DEFAULT_PAGE_NUMBER = 1;

	/** 默认每页记录数 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 最大每页记录数 */
	private static final int MAX_PAGE_SIZE = 10000;

	/** 页码 */
	@ApiModelProperty(value = "页码", dataType = "Integer", example = "1", required = true)
	@Min(value = 1, message = "页码最小为1")
	private int pageNum = DEFAULT_PAGE_NUMBER;

	/** 每页记录数 */
	@ApiModelProperty(value = "每页记录数",dataType = "Integer", example = "20", required = true)
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 搜索值 */
	@ApiModelProperty(value = "搜索值")
	@JsonIgnore
	private String keyword;

	@ApiModelProperty(value = "排序属性")
	@JsonIgnore
	private String sortField;

	@ApiModelProperty(value = "排序方向")
	@JsonIgnore
	private Direction direction;

	@ApiModelProperty(value = "筛选条件",hidden = true)
	@JsonIgnore
	@IgnoreSwaggerParameter
	private List<Filter> filters = new ArrayList<Filter>();

/*	@ApiModelProperty(value = "数据对象",hidden = true)
	private Object list;*/

	public Pageable() {
	}

}