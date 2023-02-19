/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;

/**
 * <pre>
 *   插件属性管理数据库对象
 * </pre>
 * @author zhongzm
 * @version 1.0
 */
@Data
public class SysPluginAttribute{
    //编号
    @Id
    @IgnoreSwaggerParameter
    protected String id;
    //插件编号
    @ApiModelProperty(value = "插件编号")
    protected String pluginId;
    @ApiModelProperty("类型(1.String,2.Array,3.File)")
    private String type;
    //属性名称
    @ApiModelProperty(value = "属性名称")
    protected String name;
    //属性值
    @ApiModelProperty(value = "属性值")
    protected String attribute;

}
