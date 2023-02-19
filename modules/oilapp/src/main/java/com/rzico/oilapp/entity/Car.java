/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
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
 *   车辆管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_car")
public class Car{

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "车牌号" )
    protected String plate;

    @ApiModelProperty(value = "身份证号" )
    protected String idNo;

    @ApiModelProperty(value = "审核资料" )
    protected String meta;

    @ApiModelProperty(value = "司机姓名" )
    protected String name;

    @ApiModelProperty(value = "联系电话" )
    protected String phone;

    @ApiModelProperty(value = "状态(0.待审核,1.已审核,2.已驳回)" )
    protected Integer status;

    @ApiModelProperty(value = "审核说明" )
    protected String msg;

    @ApiModelProperty(value = "所属账户" )
    protected Long companyId;


    @ApiModelProperty(value = "所属企业" )
    @Transient
    protected String companyName;


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
