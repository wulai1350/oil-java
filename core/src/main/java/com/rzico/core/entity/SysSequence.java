/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-12
*/
package com.rzico.core.entity;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 *   文件表数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
public class SysSequence{
    @ApiModelProperty(value = "编号")
    @Id
    protected String id;

    @ApiModelProperty(value = "序号")
    @Column(name = "`last_value`")
    protected Long lastValue;


}
