package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * <pre>
 *   组织机构领域对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@ApiModel(description= "组织架构")
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysOffice {

    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 组织编码
     */
    @Id
    private String id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 类型 (0公司 1部门)
     */
    private Integer type;

    /**
     * 父级编号
     */
    private String parentId;

    /**
     * 关系树
     */
    @JsonIgnore
    private String treePath;

    /**
     * 排序
     */
    private Integer dataSort;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 办公电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 状态（1启用 0停用）
     */
    private Integer status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 商户号
     */
    @JsonIgnore
    private String mchId;

    /**
     * 创建者
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建者" ,hidden = true )
    private String createBy;

    @ApiModelProperty(value = "创建时间" ,hidden = true )
    private Date createDate;

    @JsonIgnore
    @ApiModelProperty(value = "更新者" ,hidden = true )
    private String modifyBy;

    @JsonIgnore
    @ApiModelProperty(value = "更新时间" ,hidden = true )
    private Date modifyDate;

    /**
     * 删除标志
     */
    @JsonIgnore
    private Boolean delFlag;

    /**
     * 子级，需要返回给前端接口
     */
    @ApiModelProperty(value = "下级", hidden = true)
    @IgnoreSwaggerParameter
    private List<SysOffice> childrens = Collections.emptyList();

    /**
     * 当前对象的直属父级
     */
    @JsonIgnore
    @IgnoreSwaggerParameter
    private SysOffice parent;



    @ApiModelProperty(value = "名称", hidden = true)
    public String getOfficeName(){
        SysOffice sysOffice1 = getParent();
        if (null != sysOffice1){
            return sysOffice1.getName();
        }else{
            return "";
        }
    }

    public void setOfficeName(String officeName){

    }

}