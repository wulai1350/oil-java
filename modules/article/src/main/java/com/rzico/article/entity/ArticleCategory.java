/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 *   文章分类数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_article_category")
public class ArticleCategory{

    public static final String TREE_PATH_SEPARATOR = ",";

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "排序" )
    protected Integer orders;

    @ApiModelProperty(value = "名称" )
    protected String name;

    @JsonIgnore
    @ApiModelProperty(value = "" ,hidden = true )
    protected String treePath;

    @ApiModelProperty(value = "" )
    @Column(name = "parent")
    protected Long parentId;

    @ApiModelProperty(value = "缩略图" )
    protected String thumbnail;

    @ApiModelProperty(value = "子分类数组" ,hidden = true )
    @IgnoreSwaggerParameter
    private List<ArticleCategory> childrens = new ArrayList<ArticleCategory>();

    /**
     * 当前对象的直属父级
     */
    @JsonIgnore
    @IgnoreSwaggerParameter
    private ArticleCategory parent;

    @ApiModelProperty(value = "上级分类名称", hidden = true)
    public String getParentName(){
        ArticleCategory gaugeCategory = getParent();
        if (null != gaugeCategory){
            return gaugeCategory.getName();
        }else{
            return "";
        }
    }

}
