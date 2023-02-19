/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.controller;

import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysUser;
import com.rzico.core.model.PluginAttribute;
import com.rzico.core.plugin.*;
import com.rzico.core.service.SysUserService;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.service.SysPluginService;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插件管理控制层
 *
 * @author zhongzm
 * @version 1.0
 * @date 2020-02-04
 */
@Api(description = "插件管理接口")
@RestController
@RequestMapping("/admin/sysPlugin")
public class SysPluginController extends BaseController {

    @Autowired
    private SysPluginService sysPluginService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询所有插件
     * 1存储插件 2支付插件 3手机短信 4分账插件
     *
     * @return
    */

    @ApiOperation("查询所有插件")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "mchId", value = "商户编码", dataType = "String", paramType = "query")})
    public CommResult<SysPlugin> list(String mchId) {
        if (mchId==null) {
            SysUser sysUser = sysUserService.getCurrent();
            mchId = sysUser.getMchId();
        }
        List<SysPlugin> sysPlugins = new ArrayList<>();
        for (StoragePlugin storagePlugin:sysPluginService.getStoragePlugins()) {
            SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,storagePlugin.getId());
            if (sysPlugin==null) {
                sysPlugin = new SysPlugin();
                sysPlugin.setVersion(storagePlugin.getVersion());
                sysPlugin.setPluginType(1);
                sysPlugin.setPluginId(storagePlugin.getId());
                sysPlugin.setName(storagePlugin.getName());
                sysPlugin.setIsEnabled(false);
                sysPlugin.setIsInstalled(false);
            }
            sysPlugins.add(sysPlugin);
        }

        for (MsgPlugin msgPlugin:sysPluginService.getMsgPlugins()) {
            SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,msgPlugin.getId());
            if (sysPlugin==null) {
                sysPlugin = new SysPlugin();
                sysPlugin.setVersion(msgPlugin.getVersion());
                sysPlugin.setPluginType(3);
                sysPlugin.setPluginId(msgPlugin.getId());
                sysPlugin.setName(msgPlugin.getName());
                sysPlugin.setIsEnabled(false);
                sysPlugin.setIsInstalled(false);
            }
            sysPlugins.add(sysPlugin);
        }
        for (AuthPlugin oauth2Plugin:sysPluginService.getOAuth2Plugins()) {
            SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,oauth2Plugin.getId());
            if (sysPlugin==null) {
                sysPlugin = new SysPlugin();
                sysPlugin.setVersion(oauth2Plugin.getVersion());
                sysPlugin.setPluginType(4);
                sysPlugin.setPluginId(oauth2Plugin.getId());
                sysPlugin.setName(oauth2Plugin.getName());
                sysPlugin.setIsEnabled(false);
                sysPlugin.setIsInstalled(false);
            }
            sysPlugins.add(sysPlugin);
        }


        return CommResult.success(sysPlugins);
    }


    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysPlugin> find(@PathVariable String id) {

        SysPlugin result = sysPluginService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysPlugin",result);
        if (result.getPluginType()==1) {
            StoragePlugin storagePlugin = sysPluginService.getStoragePlugin(result.getPluginId());
            List<PluginAttribute> attrs = storagePlugin.getAttributeKeys();
            for (PluginAttribute attr:attrs) {
                attr.setValue(result.getAttribute(attr.getKey()));
            }
            data.put("attributeKeys",attrs);
        }

        if (result.getPluginType()==3) {
            MsgPlugin msgPlugin = sysPluginService.getMsgPlugin(result.getPluginId());
            List<PluginAttribute> attrs = msgPlugin.getAttributeKeys();
            for (PluginAttribute attr:attrs) {
                attr.setValue(result.getAttribute(attr.getKey()));
            }
            data.put("attributeKeys",attrs);
        }
        if (result.getPluginType()==4) {
            AuthPlugin oAuth2Plugin = sysPluginService.getAuthPlugin(result.getPluginId());
            List<PluginAttribute> attrs = oAuth2Plugin.getAttributeKeys();
            for (PluginAttribute attr:attrs) {
                attr.setValue(result.getAttribute(attr.getKey()));
            }
            data.put("attributeKeys",attrs);
        }


        return CommResult.success(data);
    }

    /**
     * 安装插件
     *
     * @param pluginId
     * @return
     */
    @ApiOperation("安装插件")
    @PostMapping("/save")
    @ApiImplicitParams({@ApiImplicitParam(name = "mchId", value = "商户编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pluginId", value = "插件编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "插件名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pluginType", value = "类型（1存储插件 2支付插件 3手机短信 4分账插件）", dataType = "Integer", paramType = "query")})
    public CommResult<SysPlugin> save(String mchId,String pluginId,String version,String name,Integer pluginType) {
        if (mchId==null) {
            SysUser sysUser = sysUserService.getCurrent();
            mchId = sysUser.getMchId();
        }

        SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,pluginId);
        if (sysPlugin==null) {
            sysPlugin = new SysPlugin();
            sysPlugin.setMchId(mchId);
            sysPlugin.setId(CodeGenerator.getUUID());
            sysPlugin.setPluginType(pluginType);
            sysPlugin.setPluginId(pluginId);
            sysPlugin.setName(name);
            sysPlugin.setVersion(version);
            sysPlugin.setIsEnabled(true);
            sysPlugin.setIsInstalled(true);
            sysPluginService.insert(sysPlugin);
        } else {
            return CommResult.error("已经安装");
        }

        SysPlugin result = sysPluginService.findById(sysPlugin.getId());
        return CommResult.success(result);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除记录,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysPlugin> del(@PathVariable String ids) {

        sysPluginService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 启用/禁用
     *
     * @param id
     * @param status
     * @return
     */
    @Log(desc = "启用/禁用", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("启用/禁用")
    @PostMapping("/updateStatus")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "插件id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(true.启用,false.禁用)", dataType = "Boolean", paramType = "query")
    })
    public CommResult<SysPlugin> update(String id,Boolean status) {
        SysPlugin sysPlugin = new SysPlugin();
        sysPlugin.setId(id);
        sysPlugin.setIsEnabled(status);
        int affectCount = sysPluginService.updateByPrimaryKeySelective(sysPlugin);
        if (affectCount <= 0){
            return CommResult.error();
        }
        return CommResult.success();
    }

    /**
     * 设置插件参数
     *
     * @param id
     * @param attributes
     * @return
     */
    @Log(desc = "设置插件参数", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("设置插件参数")
    @PostMapping("/update")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "插件id", dataType = "String", paramType = "query")
    })
    public CommResult<SysPlugin> update(@RequestBody List<PluginAttribute> attributes, String id) {
        int affectCount = sysPluginService.updateAtrribles(id,attributes);
        if (affectCount <= 0){
            return CommResult.error();
        }
        return CommResult.success();
    }

    /**
     * 查询安装的插件
     * 1存储插件 2支付插件 3手机短信 4分账插件
     *
     * @return
     */

    @ApiOperation("查询安装的插件")
    @GetMapping("/installList")
    @ApiImplicitParams({@ApiImplicitParam(name = "mchId", value = "商户编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pluginType", value = "插件类型（1存储插件 2支付插件 3手机短信 4.授权插件 5.付款插件,6.分账插件,7.分销插件,8.骑手插件）", dataType = "String", paramType = "query")}
    )
    public CommResult<SysPlugin> installList(String mchId,Integer pluginType ) {
        Map<String,Object> params = new HashMap<>();

        if (mchId==null) {
            SysUser sysUser = sysUserService.findByUsername("admin");
            mchId = sysUser.getMchId();
        }

        params.put("mchId",mchId);
        params.put("pluginType",pluginType);
        params.put("isEnabled",1);
        List<SysPlugin> sysPluginList = sysPluginService.selectList(params);
        return CommResult.success(sysPluginList);
    }

}
