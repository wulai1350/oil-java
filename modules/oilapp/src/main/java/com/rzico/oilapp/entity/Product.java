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
 *   产品档案数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_product")
public class Product{

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "产品编码" )
    protected String sn;

    @ApiModelProperty(value = "产品名称" )
    protected String name;

    @ApiModelProperty(value = "预售价格" )
    protected java.math.BigDecimal price;

    @ApiModelProperty(value = "所属公司" )
    protected Long companyId;

    @ApiModelProperty(value = "供应商ID" )
    protected Long customerId;

    @ApiModelProperty(value = "最小起订量" )
    protected Integer minimum;

    @ApiModelProperty(value = "锁油期限(天)" )
    protected Integer limitDay;

    @ApiModelProperty(value = "交易模式(1.锁涨不跌,2.锁涨追跌)" )
    protected Integer orderMethod;

    @ApiModelProperty(value = "商品库ID" )
    protected Long goodsId;

    @ApiModelProperty(value = "所属企业" )
    @Transient
    protected String companyName;

    @ApiModelProperty(value = "供货商" )
    @Transient
    protected String customerName;

}
