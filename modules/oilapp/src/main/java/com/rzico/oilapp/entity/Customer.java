/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.entity;

import com.alibaba.fastjson.JSON;
import com.rzico.oilapp.model.Meta;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   客户资料数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_customer")
public class Customer{

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "公司名称" )
    protected String name;

    @ApiModelProperty(value = "客户类型(1.供应商,2.采购方)" )
    protected Integer type;

    @ApiModelProperty(value = "公司类型(1.贸易公司,2.零售终端,3.用油企业)" )
    protected Integer nature;

    @ApiModelProperty(value = "公司Logo" )
    protected String logo;

    @ApiModelProperty(value = "联系电话" )
    protected String phone;

    @ApiModelProperty(value = "联系人" )
    protected String linkman;

    @ApiModelProperty(value = "状态(0.未认证,1.已认证)" )
    protected Integer status;

    @ApiModelProperty(value = "审核说明" )
    protected String msg;

    @ApiModelProperty(value = "详情地址" )
    protected String address;

    @ApiModelProperty(value = "所属集团" )
    protected Long groupId;

    @ApiModelProperty(value = "所属账户" )
    protected Long companyId;

    @ApiModelProperty(value = "审核资料" )
    protected String meta;

    @ApiModelProperty(value = "集团名称" )
    @Transient
    protected String groupName;

    public Meta getMetaInfo() {
        if (getMeta()!=null) {
            Meta meta = JSON.parseObject(getMeta(),Meta.class);
            return meta;
        } else {
            return null;
        }
    }

    public void setMetaInfo(Meta meta) {
        setMeta(JSON.toJSONString(meta));
    }
}
