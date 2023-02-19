/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
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
 *   计划任务表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysJob{

    @ApiModelProperty(value = "")
    @Id
    protected String id;

    @ApiModelProperty(value = "描述任务")
    protected String name;

    @ApiModelProperty(value = "其他描述")
    protected String jobDesc;

    @ApiModelProperty(value = "任务表达式")
    protected String cron;

    @ApiModelProperty(value = "状态:0禁用/1启用")
    protected Integer status;

    @ApiModelProperty(value = "执行状态:0未启动/1运行中")
    protected Integer execStatus;

    @ApiModelProperty(value = "错误描述")
    protected String execErrDesc;

    @ApiModelProperty(value = "执行机IP地址")
    protected String execAddr;

    @ApiModelProperty(value = "任务执行方法")
    protected String clazzPath;

    @ApiModelProperty(value = "创建者")
    protected String createBy;

    @ApiModelProperty(value = "创建时间")
    protected java.util.Date createDate;

    @ApiModelProperty(value = "更新者")
    protected String modifyBy;

    @ApiModelProperty(value = "更新时间")
    protected java.util.Date modifyDate;

}
