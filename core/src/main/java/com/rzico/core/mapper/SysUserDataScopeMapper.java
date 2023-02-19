/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-15
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysUserDataScope;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   付款单映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysUserDataScopeMapper extends BaseMapper<SysUserDataScope, String> {
   List<SysUserDataScope> selectList(Map<String, Object> params);
}
