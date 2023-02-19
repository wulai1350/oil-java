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
 *   用户角色领域对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleUser {
    /**
     *
     */
    @Id
    @IgnoreSwaggerParameter
    protected String id;
    /**
     * 
     */
    protected String userId;
    /**
     * 
     */
    protected String roleId;

}
