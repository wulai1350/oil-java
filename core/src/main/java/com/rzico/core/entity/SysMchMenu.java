/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 *   商户权限表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
public class SysMchMenu{
    @Id
    protected String id;

    @ApiModelProperty(value = "商户号")
    protected String mchId;

    @ApiModelProperty(value = "菜单编号")
    protected String menuId;


}
