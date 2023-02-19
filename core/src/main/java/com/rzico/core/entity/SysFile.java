/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Blob;
import java.util.Base64;
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
public class SysFile{
    @ApiModelProperty(value = "编号")
    @Id
    protected String id;

    @ApiModelProperty(value = "文件名称")
    protected String fileName;

    @ApiModelProperty(value = "文件分类（0image、1media、2music、3file）")
    protected Integer fileType;

    @ApiModelProperty(value = "文件路径")
    protected String fileUrl;

    @ApiModelProperty(value = "文件路径")
    @JsonIgnore
    protected byte[] content;

    @ApiModelProperty(value = "插件编码")
    protected String pluginId;

    @ApiModelProperty(value = "插件名称")
    protected String pluginName;

    @ApiModelProperty(value = "单据类型(askfor)")
    protected String orderType;

    @ApiModelProperty(value = "关联单据")
    protected Long orderId;

    @ApiModelProperty(value = "状态（0正常 1删除 2停用）")
    protected Integer status;

    @ApiModelProperty(value = "备注信息")
    @JsonIgnore
    protected String remark;

    @ApiModelProperty(value = "商户号")
    @JsonIgnore
    protected String mchId;

    @ApiModelProperty(value = "创建者")
    @JsonIgnore
    protected String createBy;

    @ApiModelProperty(value = "创建时间")
    protected java.util.Date createDate;

    @ApiModelProperty(value = "更新者")
    @JsonIgnore
    protected String modifyBy;

    @ApiModelProperty(value = "更新时间")
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "删除标志")
    @JsonIgnore
    protected Boolean delFlag;

    public void setBase64Content(String value) {
        if (value==null) {
            setContent(null);
        }
        Base64.Decoder decoder = Base64.getDecoder();
        setContent(decoder.decode(value));
    }

    public String getBase64Content() {
        if (getContent()==null) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(getContent());
    }

}
