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
 *   提货管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_shipping")
public class Shipping{
    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "所属公司" )
    protected Long companyId;

    @ApiModelProperty(value = "供应商" )
    protected Long supplierId;

    @ApiModelProperty(value = "采购商" )
    protected Long customerId;

    @ApiModelProperty(value = "油库" )
    protected String warehouseId;

    @ApiModelProperty(value = "商品" )
    protected String productId;

    @ApiModelProperty(value = "车辆id" )
    protected String carId;

    @ApiModelProperty(value = "提货数量" )
    protected java.math.BigDecimal quantity;

    @ApiModelProperty(value = "提供方式(1.公路,2.船提,3.火车)" )
    protected Integer delivery;

    @ApiModelProperty(value = "提货状态(0.制单，1.已预约,2.已提货,10.已关闭)" )
    protected Integer status;

    @ApiModelProperty(value = "备注" )
    protected String memo;


}
