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
 *   职位表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPost{
    @ApiModelProperty(value = "岗位编码")
    @Id
    protected String id;

    @ApiModelProperty(value = "岗位名称")
    protected String name;

    @ApiModelProperty(value = "父级编号")
    protected String parentId;

    @ApiModelProperty(value = "关系树")
    protected String treePath;

    @ApiModelProperty(value = "排序")
    protected Integer dataSort;

    @ApiModelProperty(value = "状态（1启用 0停用）")
    protected Integer status;

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

    @ApiModelProperty(value = "删除标志")
    protected Boolean delFlag;

}
