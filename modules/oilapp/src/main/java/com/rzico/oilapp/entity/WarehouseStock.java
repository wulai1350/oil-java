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
 *   库存管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_warehouse_stock")
public class WarehouseStock{

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
    protected Long warehouseId;

    @ApiModelProperty(value = "商品" )
    protected Long productId;

    @ApiModelProperty(value = "实物库存" )
    protected java.math.BigDecimal stock;

    @ApiModelProperty(value = "下单库存" )
    protected java.math.BigDecimal allocateStock;

    @ApiModelProperty(value = "所属企业" )
    @Transient
    protected String companyName;

    @ApiModelProperty(value = "买家名称" )
    @Transient
    protected String customerName;

    @ApiModelProperty(value = "油库名称" )
    @Transient
    protected String warehouseName;

    @ApiModelProperty(value = "产品名称" )
    @Transient
    protected String productName;

}
