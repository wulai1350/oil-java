package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* <pre>
 *   系统菜单领域对象
 * </pre>
* @author Rzico Boot
* @version 1.0
*/
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenu {

    public static final String TREE_PATH_SEPARATOR = ",";

    @Id
    protected String id;
    //
    protected String name;
    //
    protected String parentId;
    //
    protected String url;
    //排序字段
    protected Integer dataSort;
    //
    protected String treePath;
    //图标
    protected String icon;

    @JsonIgnore
    @ApiModelProperty(value = "创建者" ,hidden = true )
    protected String createBy;

    @ApiModelProperty(value = "创建时间" ,hidden = true )
    protected Date createDate;

    @JsonIgnore
    @ApiModelProperty(value = "更新者" ,hidden = true )
    protected String modifyBy;

    @JsonIgnore
    @ApiModelProperty(value = "更新时间" ,hidden = true )
    protected Date modifyDate;

    //权限
    @ApiModelProperty(value = "权限"  )
    protected String permission;

    //0菜单 1按钮
    @ApiModelProperty(value = "0菜单 1按钮"  )
    protected Integer menuType;

    @ApiModelProperty(value = "子菜单数组" ,hidden = true )
    @IgnoreSwaggerParameter
    private List<SysMenu> childrens = new ArrayList<SysMenu>();

}
