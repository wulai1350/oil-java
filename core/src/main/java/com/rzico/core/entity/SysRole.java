package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;

/**
* <pre>
 *   系统角色领域对象
 * </pre>
* @author Rzico Boot
* @version 1.0
*/
@Data
@ApiModel(description= "角色")
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRole{
    @Id
    @ApiModelProperty(value = "角色ID")
    protected String id;

    @ApiModelProperty(value = "角色名称")
    protected String name;

    @ApiModelProperty(value = "状态")
    protected Integer status;
    //
    @ApiModelProperty(value = "备注")
    protected String remark;
    //
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected String createBy;

    @ApiModelProperty(hidden = true)
    protected Date createDate;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected String modifyBy;

    @ApiModelProperty(hidden = true)
    protected Date modifyDate;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected String mchId;

    @ApiModelProperty(hidden = true)
    @IgnoreSwaggerParameter
    @JsonIgnore
    protected List<SysMenu> menuList = Collections.EMPTY_LIST;

    @Transient
    protected List<String> menus = Collections.EMPTY_LIST;

}
