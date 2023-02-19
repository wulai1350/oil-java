/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-05
*/
package com.rzico.core.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   设计器数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "sys_icon")
public class SysIcon{
    @Id
    @ApiModelProperty(value = "" )
    protected String id;

    @ApiModelProperty(value = "图标名" )
    protected String name;

    @ApiModelProperty(value = "分组(1.图标,2.素材)" )
    protected String iconType;

    @ApiModelProperty(value = "队列描述" )
    protected String iconUrl;

    @ApiModelProperty(value = "排序" )
    private Integer dataSort;

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


}
