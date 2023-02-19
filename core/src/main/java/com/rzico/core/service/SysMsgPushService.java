/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-06-11
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysMsgPush;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysMsgPushMapper;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 消息通讯业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysMsgPushService extends BaseServiceImpl<SysMsgPush, String> {

    @Autowired
    private SysMsgPushMapper sysMsgPushMapper;

    @Override
    public BaseMapper<SysMsgPush, String> getMapper() {
        return sysMsgPushMapper;
    }

    public List<SysMsgPush> conversation(Map<String, Object> params) {
        return sysMsgPushMapper.conversation(params);
    }

    public int markAsRead(Map<String, Object> params) {
        return sysMsgPushMapper.markAsRead(params);
    }
    public List<Map> unReadCount(Map<String, Object> params) {
        return sysMsgPushMapper.unReadCount(params);
    }

}
