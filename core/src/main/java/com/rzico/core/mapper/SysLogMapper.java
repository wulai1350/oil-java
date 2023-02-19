/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
/**
 * <pre>
 *   日志表映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog, String> {

}
