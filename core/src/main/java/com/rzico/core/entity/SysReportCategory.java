/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-20
*/
package com.rzico.core.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
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
@Table(name = "sys_report_category")
public class SysReportCategory{

    public static final String TREE_PATH_SEPARATOR = ",";

    @Id
    @ApiModelProperty(value = "" )
    protected String id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "排序" )
    protected Integer orders;

    @ApiModelProperty(value = "名称" )
    protected String name;

    @JsonIgnore
    @ApiModelProperty(value = "树路径" ,hidden = true )
    protected String treePath;

    @ApiModelProperty(value = "" )
    protected String parentId;

    @ApiModelProperty(value = "缩略图" )
    protected String thumbnail;

    @ApiModelProperty(value = "是否显示" )
    protected Boolean isShow;

    @JsonIgnore
    @ApiModelProperty(value = "商户号" ,hidden = true )
    protected String mchId;

    @JsonIgnore
    @ApiModelProperty(value = "创建者" ,hidden = true )
    protected String createBy;

    @JsonIgnore
    @ApiModelProperty(value = "更新者" ,hidden = true )
    protected String modifyBy;


    /**
     * 子级，需要返回给前端接口
     */
    @ApiModelProperty(value = "下级", hidden = true)
    @IgnoreSwaggerParameter
    private List<SysReportCategory> childrens = Collections.emptyList();

    /**
     * 当前对象的直属父级
     */
    @JsonIgnore
    @IgnoreSwaggerParameter
    private SysReportCategory parent;



    @ApiModelProperty(value = "名称", hidden = true)
    public String getParentName(){
        SysReportCategory sysReportCategory = getParent();
        if (null != sysReportCategory){
            return sysReportCategory.getName();
        }else{
            return "";
        }
    }

    public void setParentName(String parentName){

    }

}
