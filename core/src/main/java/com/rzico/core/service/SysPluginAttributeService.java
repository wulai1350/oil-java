/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysPluginAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysPluginAttributeMapper;

/**
 * <pre>
 * 插件属性管理业务类
 * </pre>
 *
 * @author zhongzm
 * @version 1.0
 */
@Service
public class SysPluginAttributeService extends BaseServiceImpl<SysPluginAttribute, String> {

    @Autowired
    private SysPluginAttributeMapper sysPluginAttributeMapper;

    @Override
    public BaseMapper<SysPluginAttribute, String> getMapper() {
        return sysPluginAttributeMapper;
    }

}
