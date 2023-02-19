/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-15
*/
package com.rzico.core.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 *   付款单数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
public class SysUserDataScope{
    @ApiModelProperty(value = "控制用户编码")
    @Id
    @IgnoreSwaggerParameter
    protected String id;

    @ApiModelProperty(value = "控制用户编码")
    protected String userId;

    @ApiModelProperty(value = "权限类型（1组织架构 2门店仓库..可扩展)")
    protected Integer scopeType;

    @ApiModelProperty(value = "控制组织架构")
    protected String officeId;


}
