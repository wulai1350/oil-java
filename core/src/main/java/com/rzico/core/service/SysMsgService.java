/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.service;

import com.alibaba.fastjson.JSON;
import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.*;
import com.rzico.core.plugin.MsgPlugin;
import com.rzico.exception.CustomException;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysMsgMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Templates;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 文件表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysMsgService extends BaseServiceImpl<SysMsg, String> {

    @Autowired
    private SysMsgMapper sysMsgMapper;

    @Autowired
    private SysTemplateService sysTemplateService;

    @Autowired
    private SysPluginService sysPluginService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysEmployeeService sysEmployeeService;

    @Autowired
    private SysMsgPushService sysMsgPushService;

    @Override
    public BaseMapper<SysMsg, String> getMapper() {
        return sysMsgMapper;
    }

    //发送微信模块消息
    // touser 接收用户
    // url 点击跳转链接
    public void sendMPMsg(SysTemplate msg,Map<String,Object> data) {

        Map<String,Object> model = new HashMap<>();
        model.put("touser",data.get("touser"));
        model.put("template_id",msg.getTplId());
        model.put("url",data.get("url"));
        model.put("topcolor","#FF0000");
        model.put("data",sysTemplateService.encodeMsg(msg,data));
        SysPlugin sysPlugin = sysPluginService.findByPlugin(msg.getMchId(),"weixinMPPlugin");
        if (sysPlugin==null) {
            throw new CustomException("没有安装插件");
        }

        MsgPlugin msgPlugin = sysPluginService.getMsgPlugin(sysPlugin.getPluginId());
        try {
            msgPlugin.sendMsg(sysPlugin, data.get("touser").toString(),JSON.toJSONString(model));
        } catch (Exception e) {
            throw new CustomException("发送失败");
        }

    }

    //生成sysMsgPush数据
    @Transactional(rollbackFor = Exception.class)
    public void save (SysMsgPush sysMsgPush, String[]officeIds, String[]postIds){
        SysUser sysUser = sysUserService.getCurrent();
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", sysUser.getMchId());
        params.put("officeIds", officeIds);
        params.put("postIds", postIds);
        List<SysEmployee> sysEmployees = sysEmployeeService.selectList(params);
        if (sysEmployees != null) {
            for (SysEmployee employee : sysEmployees) {
                sysMsgPush.setId(CodeGenerator.getUUID());
                sysMsgPush.setReceiveUserId(employee.getUserId());//接收者id
                sysMsgPush.setReceiveUserName(employee.getName());//接收者名
                sysMsgPushService.insert(sysMsgPush);
            }
        }
    }

}
