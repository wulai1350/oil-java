/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-08-28
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysBindUser;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   绑定用户映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysBindUserMapper extends BaseMapper<SysBindUser, String> {
   List<SysBindUser> selectList(Map<String, Object> params);
   int selectRowCount(Map<String, Object> params);
}
