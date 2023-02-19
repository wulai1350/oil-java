/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   油库管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_warehouse")
public class Warehouse {

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "油库名称" )
    protected String name;

    @ApiModelProperty(value = "油库编码" )
    protected String code;

    @ApiModelProperty(value = "所属公司" )
    protected Long companyId;

    @ApiModelProperty(value = "供应商ID" )
    protected String customerId;

    @ApiModelProperty(value = "所属城市" )
    protected Integer areaId;

    @ApiModelProperty(value = "详情地址" )
    protected String address;

    @ApiModelProperty(value = "联系电话" )
    protected String phone;

    @ApiModelProperty(value = "联系人" )
    protected String linkman;

    @ApiModelProperty(value = "所属企业" )
    @Transient
    protected String companyName;

    @ApiModelProperty(value = "供货商" )
    @Transient
    protected String customerName;

}
