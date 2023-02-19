/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.entity.SysPluginAttribute;
import com.rzico.core.model.PluginAttribute;
import com.rzico.core.plugin.*;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysPluginMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <pre>
 * 插件管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysPluginService extends BaseServiceImpl<SysPlugin, String> {

    @Resource
    private List<StoragePlugin> storagePlugins = new ArrayList<StoragePlugin>();
    @Resource
    private Map<String, StoragePlugin> storagePluginMap = new HashMap<String, StoragePlugin>();

    @Resource
    private List<MsgPlugin> msgPlugins = new ArrayList<MsgPlugin>();
    @Resource
    private Map<String, MsgPlugin> msgPluginMap = new HashMap<String, MsgPlugin>();

    @Resource
    private List<AuthPlugin> authPlugins = new ArrayList<AuthPlugin>();
    @Resource
    private Map<String, AuthPlugin> authPluginMap = new HashMap<String, AuthPlugin>();

    @Autowired
    private SysPluginAttributeService sysPluginAttributeService;

    @Autowired
    private SysPluginMapper sysPluginMapper;

    @Override
    public BaseMapper<SysPlugin, String> getMapper() {
        return sysPluginMapper;
    }

    public List<StoragePlugin> getStoragePlugins() {
        List<StoragePlugin> result = new ArrayList<StoragePlugin>();
        result.addAll(storagePlugins);
        Collections.sort(result);
        return result;
    }

    public List<MsgPlugin> getMsgPlugins() {
        List<MsgPlugin> result = new ArrayList<MsgPlugin>();
        result.addAll(msgPlugins);
        Collections.sort(result);
        return result;
    }

    public List<AuthPlugin> getOAuth2Plugins() {
        List<AuthPlugin> result = new ArrayList<AuthPlugin>();
        result.addAll(authPlugins);
        Collections.sort(result);
        return result;
    }

    public StoragePlugin getStoragePlugin(String id) {
        return storagePluginMap.get(id);
    }

    public MsgPlugin getMsgPlugin(String id) {
        return msgPluginMap.get(id);
    }

    public AuthPlugin getAuthPlugin(String id) {
        return authPluginMap.get(id);
    }

    public SysPlugin find(String id){
        return sysPluginMapper.find(id);
    }

    public SysPlugin findByPlugin(String mchId,String pluginId){
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("pluginId",pluginId);
        List<SysPlugin> sysPlugins = sysPluginMapper.selectList(params);
        if (sysPlugins.size()>0) {
            return sysPlugins.get(0);
        } else {
            return null;
        }
    }

    public SysPlugin getEnabledPlugin(String mchId,Integer pluginType) {
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("pluginType", pluginType);
        params.put("isEnabled",true);
        params.put("isInstalled", true);
        List<SysPlugin> sysPlugins = sysPluginMapper.selectList(params);
        for (SysPlugin sysPlugin:sysPlugins) {
            return sysPlugin;
        }
        return null;
    }

    public SysPlugin getEnabledPlugin(String mchId,String pluginId) {
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("isEnabled",true);
        params.put("isInstalled", true);
        List<SysPlugin> sysPlugins = sysPluginMapper.selectList(params);
        for (SysPlugin sysPlugin:sysPlugins) {
            if (sysPlugin.getPluginId().startsWith(pluginId)) {
                return sysPlugin;
            }
        }
        return null;
    }

    public SysPlugin getInstallPlugin(String mchId,String pluginId) {
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("isInstalled", true);
        List<SysPlugin> sysPlugins = sysPluginMapper.selectList(params);
        for (SysPlugin sysPlugin:sysPlugins) {
            if (sysPlugin.getPluginId().startsWith(pluginId)) {
                return sysPlugin;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateAtrribles(String id,List<PluginAttribute> attributes) {
        SysPluginAttribute sysPluginAttribute = new SysPluginAttribute();
        sysPluginAttribute.setPluginId(id);
        sysPluginAttributeService.delete(sysPluginAttribute);
        Integer rw = 0;
        if (attributes != null){
            for (PluginAttribute attribute : attributes) {
                if (attribute.getValue()!=null) {
                    SysPluginAttribute pluginAttribute = new SysPluginAttribute();
                    pluginAttribute.setId(CodeGenerator.getUUID());
                    pluginAttribute.setPluginId(id);
                    pluginAttribute.setName(attribute.getKey());
                    pluginAttribute.setAttribute(attribute.getValue());
                    pluginAttribute.setType(attribute.getType());
                    sysPluginAttributeService.insert(pluginAttribute);
                    rw = rw + 1;
                }
            }
        }
        return rw;
    }
}
