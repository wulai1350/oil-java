/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.entity;

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
public class SysMsg{
    @ApiModelProperty(value = "编号")
    @Id
    protected String id;

    @ApiModelProperty(value = "消息标题")
    protected String msgTitle;

    @ApiModelProperty(value = "内容级别（1普通 2一般 3紧急）")
    protected Integer msgLevel;

    @ApiModelProperty(value = "内容类型（1公告 2新闻 3会议 4通知）")
    protected Integer msgType;

    @ApiModelProperty(value = "消息内容")
    protected String msgContent;

    @ApiModelProperty(value = "接受者类型（0全部 1用户 2部门 3角色 4岗位）")
    protected String receiveType;

    @ApiModelProperty(value = "发送者用户编码")
    protected String sendUserId;

    @ApiModelProperty(value = "发送者用户姓名")
    protected String sendUserName;

    @ApiModelProperty(value = "发送时间")
    protected java.util.Date sendDate;

    @ApiModelProperty(value = "是否有附件(0 没有，1有附近)" )
    protected String isAttac;

    @ApiModelProperty(value = "状态（0草稿 1审核 2驳回）")
    protected Integer status;

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

    @ApiModelProperty(value = "删除标志")
    protected Boolean delFlag;


}
