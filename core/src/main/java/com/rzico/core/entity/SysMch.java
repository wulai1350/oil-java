package com.rzico.core.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@ApiModel(description= "商户号")
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMch {

    /**
     * 状态.待审核
     */
    public static Integer STATUS_UNAUDITED=0;
    /**
     * 状态.已启用
     */
    public static Integer STATUS_ENABLED=1;
    /**
     * 状态.已禁用
     */
    public static Integer STATUS_DISBALED=2;
    /**
     * 状态.初始化
     */
    public static Integer STATUS_INIT_DATA=3;

    /**
     * 商户编码
     */
    @Id
    private String id;

    /**
     * 公司全称
     */
    private String name;

    /**
     * 商户logo
     */
    private String logo;

    /**
     * 商户简称
     */
    private String shortName;

    /**
     * 状态(0 待审 1启用 2停用)
     */
    private Integer status;

    /**
     * 行政区编码
     */
    private Integer areaId;

    /**
     * 行政区名称
     */
    private String areaName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 客服电话
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 超级管理员
     */
    private String adminId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date modifyDate;

    @ApiModelProperty(hidden = true)
    @IgnoreSwaggerParameter
    @JsonIgnore
    protected List<SysMenu> menuList = Collections.EMPTY_LIST;

    /**
     * 删除状态
     */
    private Boolean delFlag;


}