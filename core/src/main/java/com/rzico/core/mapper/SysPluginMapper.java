/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysPlugin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *   插件管理映射类
 * </pre>
 *
 * @author zhongzm
 * @version 1.0
 */
@Mapper
public interface SysPluginMapper extends BaseMapper<SysPlugin, String> {

    public SysPlugin find(String id);
}
