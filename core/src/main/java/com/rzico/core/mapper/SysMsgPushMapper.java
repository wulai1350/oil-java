/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-06-11
*/
package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysMsgPush;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   消息通讯映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysMsgPushMapper extends BaseMapper<SysMsgPush, String> {
   List<SysMsgPush> selectList(Map<String, Object> params);
   List<SysMsgPush> conversation(Map<String, Object> params);
   int selectRowCount(Map<String, Object> params);
   int markAsRead(Map<String, Object> params);
   List<Map> unReadCount(Map<String, Object> params);
}
