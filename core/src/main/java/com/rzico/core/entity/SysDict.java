/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-16
*/
package com.rzico.core.entity;

import lombok.Data;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   数据字典数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "sys_dict")
public class SysDict{

    @Id
    @ApiModelProperty(value = "字典id" )
    protected String id;

    @ApiModelProperty(value = "字典名称" )
    protected String name;

    @ApiModelProperty(value = "dict_type表主键" )
    protected String dictType;

    @ApiModelProperty(value = "描述" )
    protected String description;

    @ApiModelProperty(value = "状态(1启用 0停用)" )
    protected Integer status;

    @JsonIgnore
    @ApiModelProperty(value = "创建者" ,hidden = true )
    protected String createBy;

    @ApiModelProperty(value = "创建时间" )
    protected java.util.Date createDate;

    @JsonIgnore
    @ApiModelProperty(value = "更新者" ,hidden = true )
    protected String modifyBy;

    @ApiModelProperty(value = "更新时间" )
    protected java.util.Date modifyDate;

    @JsonIgnore
    @ApiModelProperty(value = "删除标志" ,hidden = true )
    protected Boolean delFlag;


}
