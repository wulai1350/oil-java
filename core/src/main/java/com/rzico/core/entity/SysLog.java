/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 *   日志表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
public class SysLog {

    @ApiModelProperty(value = "")
    @Id
    protected String id;

    @ApiModelProperty(value = "用户账号")
    protected String username;

    @ApiModelProperty(value = "日志类型")
    protected String logType;

    @ApiModelProperty(value = "日志标题")
    protected String logTitle;

    @ApiModelProperty(value = "请求URI")
    protected String requestUri;

    @ApiModelProperty(value = "操作方式")
    protected String requestMethod;

    @ApiModelProperty(value = "操作提交的数据")
    protected String requestParams;

    @ApiModelProperty(value = "新旧数据比较结果")
    protected String diffModifyData;

    @ApiModelProperty(value = "操作IP地址")
    protected String remoteAddr;

    @ApiModelProperty(value = "请求服务器地址")
    protected String serverAddr;

    @ApiModelProperty(value = "是否异常")
    protected String isException;

    @ApiModelProperty(value = "异常信息")
    protected String exceptionInfo;

    @ApiModelProperty(value = "用户代理")
    protected String userAgent;

    @ApiModelProperty(value = "设备名称/操作系统")
    protected String deviceName;

    @ApiModelProperty(value = "浏览器名称")
    protected String browserName;

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
