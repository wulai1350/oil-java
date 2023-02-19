/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 *   文件表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysTemplate{

    @ApiModelProperty(value = "编号")
    @Id
    protected String id;

    @ApiModelProperty(value = "模板键值")
    protected String tplKey;

    @ApiModelProperty(value = "模板消息id")
    protected String tplId;

    @ApiModelProperty(value = "模板名称")
    protected String tplName;

    @ApiModelProperty(value = "状态（0短信 1邮箱 2通知）")
    protected Integer tplType;

    @ApiModelProperty(value = "模板内容")
    protected String tplContent;

    @ApiModelProperty(value = "备注信息")
    protected String remark;

    @ApiModelProperty(value = "商户号")
    protected String mchId;

    @ApiModelProperty(value = "创建者")
    protected String createBy;

    @ApiModelProperty(value = "创建时间")
    protected java.util.Date createDate;

    @ApiModelProperty(value = "更新者")
    protected String modifyBy;

    @ApiModelProperty(value = "更新时间")
    protected java.util.Date modifyDate;

}
