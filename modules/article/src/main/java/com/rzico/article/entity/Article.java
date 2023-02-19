/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.entity;

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
 *   文章管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "wx_article")

public class Article{

    @Id
    @ApiModelProperty(value = "" )
    protected Long id;

    @ApiModelProperty(value = "创建日期" )
    protected java.util.Date createDate;

    @ApiModelProperty(value = "修改日期" )
    protected java.util.Date modifyDate;

    @ApiModelProperty(value = "谁可见{0:公开,1:不公开,2:加密,3:私秘}" )
    protected Integer authority;

    @ApiModelProperty(value = "" )
    protected Integer orders;

    @ApiModelProperty(value = "文章内容" )
    protected String content;

    @ApiModelProperty(value = "阅读数" )
    protected Long hits;

    @ApiModelProperty(value = "阅读时间" )
    protected Integer readTime;

    @ApiModelProperty(value = "类型 {0:图文,1:视频}" )
    protected Integer articleType;

    @ApiModelProperty(value = "标题" )
    protected String title;

    @ApiModelProperty(value = "分类" )
    protected Long articleCategoryId;

    @JsonIgnore
    @ApiModelProperty(value = "是否删除" ,hidden = true )
    protected Boolean deleted;

    @ApiModelProperty(value = "缩略图" )
    protected String thumbnail;

    @ApiModelProperty(value = "是否草稿" )
    protected Boolean isDraft;

    @ApiModelProperty(value = "其他资源" )
    protected String meta;

    /**
     * 所属分类
     */
    @JsonIgnore
    @IgnoreSwaggerParameter
    protected ArticleCategory articleCategory;

    @ApiModelProperty(value = "分类名" )
    public String getArticleCategoryName() {
        ArticleCategory mgc = getArticleCategory();
        if (mgc!=null) {
            return mgc.getName();
        } else {
            return "";
        }
    }

}
