/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-06-11
*/
package com.rzico.core.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   消息通讯数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "sys_msg_push")
public class SysMsgPush{
    @Id
    @ApiModelProperty(value = "编号" )
    protected String id;

    @ApiModelProperty(value = "消息编码" )
    protected String msgId;

    @ApiModelProperty(value = "关联业务类型" )
    protected String msgKey;

    @ApiModelProperty(value = "内容级别（1普通 2一般 3紧急）" )
    protected Integer msgLevel;

    @ApiModelProperty(value = "内容类型（1公告 2新闻 3会议 4通知）" )
    protected Integer msgType;

    @ApiModelProperty(value = "消息标题" )
    protected String msgTitle;

    @ApiModelProperty(value = "消息内容（通知时是json串）" )
    protected String msgContent;

    @ApiModelProperty(value = "是否有附件(0 没有，1有附近)" )
    protected String isAttac;

    @ApiModelProperty(value = "接受者用户编码" )
    protected String receiveUserId;

    @ApiModelProperty(value = "接受者用户姓名" )
    protected String receiveUserName;

    @ApiModelProperty(value = "发送者用户编码" )
    protected String sendUserId;

    @ApiModelProperty(value = "发送者用户姓名" )
    protected String sendUserName;

    @ApiModelProperty(value = "发送时间" )
    protected java.util.Date sendDate;

    @ApiModelProperty(value = "是否推送" )
    protected Boolean isPush;

    @ApiModelProperty(value = "推送时间" )
    protected java.util.Date pushDate;

    @ApiModelProperty(value = "推送状态（0未推送 1成功  2失败）" )
    protected String pushStatus;

    @ApiModelProperty(value = "读取状态（0未送达 1已读 2未读）" )
    protected String readStatus;

    @ApiModelProperty(value = "读取时间" )
    protected java.util.Date readDate;

    @JsonIgnore
    @ApiModelProperty(value = "商户号" ,hidden = true )
    protected String mchId;


}
