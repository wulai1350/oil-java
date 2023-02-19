/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import com.rzico.core.entity.SysOffice;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   交易订单数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_order")
public class Order{

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

    @ApiModelProperty(value = "产品" )
    protected Long productId;

    @ApiModelProperty(value = "油库" )
    protected Long warehouseId;

    @ApiModelProperty(value = "订单状态(0.制单,1.接单,2.复核,3.出单,4.下定,5.付款,9.完成,10.关闭)" )
    protected Integer status;

    @ApiModelProperty(value = "油品单价" )
    protected java.math.BigDecimal price;

    @ApiModelProperty(value = "加价售销" )
    protected java.math.BigDecimal addPrice;

    @ApiModelProperty(value = "应付金额")
    protected java.math.BigDecimal amountPayable;

    @ApiModelProperty(value = "已付金额")
    protected java.math.BigDecimal amountPaid;

    @ApiModelProperty(value = "购买吨数" )
    protected java.math.BigDecimal quantity;

    @ApiModelProperty(value = "提供方式(1.车提,2.船提,3.火车)" )
    protected Integer delivery;

    @ApiModelProperty(value = "交易模式(1.锁涨不跌,2.锁涨追跌)" )
    protected Integer orderMethod;

    @ApiModelProperty(value = "客户经理" )
    protected String linkman;

    @ApiModelProperty(value = "联系电话" )
    protected String phone;

    @ApiModelProperty(value = "备注" )
    protected String memo;

    @ApiModelProperty(value = "锁油期限(天)" )
    protected Integer limitDay;

    /**
     * 供货方
     */

    @IgnoreSwaggerParameter
    private Customer seller;



    /**
     * 采购方
     */

    @IgnoreSwaggerParameter
    private Customer buyyer;


    /**
     * 油库
     */

    @IgnoreSwaggerParameter
    private Warehouse warehouse;


    /**
     * 产品
     */

    @IgnoreSwaggerParameter
    private Product product;


}
