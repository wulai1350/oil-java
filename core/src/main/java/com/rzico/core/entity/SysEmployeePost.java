/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.entity;

import com.rzico.annotation.IgnoreSwaggerParameter;
import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <pre>
 *   人员职位表领域对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysEmployeePost {
    /**
     * 员工编码
     */
    @Id
    @IgnoreSwaggerParameter
    protected String id;
    /**
     * 员工编码
     */
    protected String employeeId;
    /**
     * 岗位编码
     */
    protected String postId;

}
