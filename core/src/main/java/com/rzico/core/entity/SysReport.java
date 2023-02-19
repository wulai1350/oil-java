/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-20
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
 *   统计报表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "sys_report")
public class SysReport{
    @Id
    @ApiModelProperty(value = "报表编码" )
    protected String id;

    @ApiModelProperty(value = "缩略图" )
    protected String thumbnail;

    @ApiModelProperty(value = "报表名称" )
    protected String name;

    @ApiModelProperty(value = "分类编号" )
    protected String reportCategoryId;

    @ApiModelProperty(value = "状态（1启用 0停用）" )
    protected Integer status;

    @ApiModelProperty(value = "数据源SQL" )
    @Column(name = "`sql`")
    protected String sql;

    @ApiModelProperty(value = "参数定议" )
    protected String params;

    @ApiModelProperty(value = "报表模板" )
    protected String template;

    @ApiModelProperty(value = "备注信息" )
    protected String remark;

    @JsonIgnore
    @ApiModelProperty(value = "商户号" ,hidden = true )
    protected String mchId;

    @JsonIgnore
    @ApiModelProperty(value = "创建者" ,hidden = true )
    protected String createBy;

    @ApiModelProperty(value = "创建时间" )
    protected java.util.Date createDate;

    @JsonIgnore
    @ApiModelProperty(value = "更新者" ,hidden = true )
    protected String modifyBy;

    @ApiModelProperty(value = "更新时间" )
    protected java.util.Date modifyDate;

    @JsonIgnore
    @ApiModelProperty(value = "删除标志" ,hidden = true )
    protected Boolean delFlag;


}
