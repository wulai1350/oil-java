/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
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
 *   操作日期数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_manager")
public class Manager{

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "客户经理" )
    protected String name;

    @ApiModelProperty(value = "联系电话" )
    protected String phone;

    @ApiModelProperty(value = "状态(0.待审核,1.已审核,2.已驳回)" )
    protected Integer status;

    @ApiModelProperty(value = "审核说明" )
    protected String msg;

    @ApiModelProperty(value = "供应商ID" )
    protected String customerId;

    @ApiModelProperty(value = "供货商" )
    @Transient
    protected String customerName;

}
