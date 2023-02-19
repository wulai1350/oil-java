package com.rzico.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  插件属性
 * Created by macro on 2018/5/17.
 */
@Data
public class PluginAttribute {

    public static String STRING_VALUE = "1";
    public static String ARRAY_VALUE = "2";
    public static String FILE_VALUE = "3";

    @ApiModelProperty("类型(1.String,2.Array,3.File)")
    private String type;
    @ApiModelProperty("属性名")
    private String key;
    @ApiModelProperty("标题名")
    private String title;
    @ApiModelProperty("可选项(如:1.普通商户,2.特约商户)")
    private String options;
    @ApiModelProperty("属性值")
    private String value;
    @ApiModelProperty("是否必填")
    private Boolean required;

    public PluginAttribute() {
        this.required = true;
    }

    public PluginAttribute(String key,String title) {
        this.key = key;
        this.title = title;
        this.type = "1";
        this.required = true;
    }

    public PluginAttribute(String key,String title,String type) {
        this.key = key;
        this.title = title;
        this.type = type;
        this.options = "";
        this.required = true;
    }

    public PluginAttribute(String key,String title,String type,String options) {
        this.key = key;
        this.title = title;
        this.type = type;
        this.options = options;
        this.required = true;
    }

    public PluginAttribute(String key,String title,String type,String options,Boolean required) {
        this.key = key;
        this.title = title;
        this.type = type;
        this.options = options;
        this.required = required;
    }

}
