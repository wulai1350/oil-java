/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysEmployee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *   人员表映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysEmployeeMapper extends BaseMapper<SysEmployee, String> {

    public List<SysEmployee> selectList(Map<String, Object> map);

    public SysEmployee find(String id);
}
