package com.rzico.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.*;
import com.rzico.core.mapper.SysUserMapper;
import com.rzico.exception.CustomException;
import com.rzico.util.CodeGenerator;
import com.rzico.util.RedisHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
<pre>
 * 系统用户业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysUserService extends BaseServiceImpl<SysUser, String> {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysRoleUserService sysRoleUserService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysMchService sysMchService;

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public BaseMapper<SysUser, String> getMapper() {
        return sysUserMapper;
    }

    public SysUser login(String username){
        return sysUserMapper.login(username);
    }

    public SysUser getCurrent() {
        Map<String,String> sysUser = super.getToken();
        if (sysUser!=null) {
            return findSysUser(sysUser.get("id"));
        }
        return null;
    }

    public SysUser findSysUser(String id) {
/*

        String key = "sysUser:"+id;
        SysUser sysUser = (SysUser)redisHandler.get(key);
        if (null != sysUser){
            return sysUser;
        }else{
*/

        SysUser   sysUser = sysUserMapper.find(id);
/*

            redisHandler.set(key, sysUser, RedisHandler.CACHE_EXPIRED_TIME_TWO_HOURS);
        }
*/

        return sysUser;
    }

//    @CachePut(value = "sysUser", key = "#record.id", unless = "#result eq null ")
    public SysUser update(SysUser record) {
        SysUser sysUser = null;
        int affectCount = super.updateByPrimaryKeySelective(record);
        if (affectCount > 0){
            sysUser = this.findById(record.getId());
        }

        return sysUser;
    }

    //检查是否管理员及二级管理
    public Boolean isAdmin(SysUser sysUser) {
        SysMch sysMch = sysMchService.selectByPrimaryKey(sysUser.getMchId());
        return sysUser.getId().equals(sysMch.getAdminId());
    }

    //检查是否超级管理员
    public Boolean isSuperAdmin(SysUser sysUser) {
        SysMch sysMch = sysMchService.selectByPrimaryKey(sysUser.getMchId());
        SysUser admin = sysUserMapper.selectByPrimaryKey(sysMch.getAdminId());
        return "admin".equals(admin.getUsername());
    }

    public Map<String, Object> getInfo(SysUser sysUser){
        Map<String,Object> data = new HashMap<>();
        data.put("userInfo",sysUser);
        if (sysUser.getAvatar()==null && sysUser.getMchId()!=null) {
            SysMch sysMch = sysMchService.findById(sysUser.getMchId());
            sysUser.setAvatar(sysMch.getLogo());
            updateByPrimaryKeySelective(sysUser);
        }
        data.put("mchId", sysUser.getMchId());
        if (sysUser.getUserType().equals(1)) {
            SysMch sysMch = sysMchService.findById(sysUser.getMchId());
            data.put("status", sysMch.getStatus());
            if ("admin".equals(sysUser.getUsername())) {
                List<SysMenu> list = sysMenuService.selectList(null);
                List<String> menus = new ArrayList<>();
                for (SysMenu menu : list) {
                    menus.add(menu.getPermission());
                }
                data.put("menus", menus);
                redisHandler.set("menu_"+sysUser.getId(),menus);
            } else
            if (isAdmin(sysUser)) {
                //改 本商户开通的所有权限，不能是全表菜单
                List<SysMenu> list = sysMch.getMenuList();
                List<String> menus = new ArrayList<>();
                for (SysMenu menu : list) {
                    menus.add(menu.getPermission());
                }
                data.put("menus", menus);
                redisHandler.set("menu_"+sysUser.getId(),menus);
            } else {
                List<SysMenu> list = sysUser.getMenuList();
                List<String> menus = new ArrayList<>();
                for (SysMenu menu : list) {
                    menus.add(menu.getPermission());
                }
                data.put("menus", menus);
                redisHandler.set("menu_"+sysUser.getId(),menus);
            }
        } else {
            data.put("status", 0);
        }
        return data;
    }

    public boolean checkPermission(String permission){
        Map<String,String> sysUser = super.getToken();
        if (sysUser==null) {
            throw new CustomException("登录已过期");
        }

        if (!redisHandler.exists("menu_"+sysUser.get("id"))) {
            throw new CustomException("登录已过期");
        }

        List<String> menuList = (List) redisHandler.get("menu_"+sysUser.get("id"));
        return menuList.contains(permission);
    }

    public int checkUserExists(String username){
        return sysUserMapper.checkUserExists(username);
    }

    public SysUser findByUsername(String username){
        Map<String,Object> params = new HashMap<>();
        params.put("username",username);
        List<SysUser> sysUsers = selectList(params);
        if (sysUsers.size()>0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }

    public SysUser findByCode(Long codeId){
        Map<String,Object> params = new HashMap<>();
        params.put("codeId",codeId);
        List<SysUser> sysUsers = selectList(params);
        if (sysUsers.size()>0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }

    public SysUser findByWxId(String mchId,String wxId){
        Map<String,Object> params = new HashMap<>();
        if (mchId!=null) {
            params.put("userType",2);
            params.put("mchId",mchId);
        } else {
            params.put("userType",1);
        }
        params.put("wxId",wxId);
        List<SysUser> sysUsers = selectList(params);
        if (sysUsers.size()>0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }


    public SysUser findByAliId(String mchId,String aliId){
        Map<String,Object> params = new HashMap<>();
        if (mchId!=null) {
            params.put("userType",2);
            params.put("mchId",mchId);
        } else {
            params.put("userType",1);
        }
        params.put("aliId",aliId);
        List<SysUser> sysUsers = selectList(params);
        if (sysUsers.size()>0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }
    public SysUser findByWxmId(String mchId,String wxmId){
        Map<String,Object> params = new HashMap<>();
        if (mchId!=null) {
            params.put("userType",2);
            params.put("mchId",mchId);
        } else {
            params.put("userType",1);
        }
        params.put("wxmId",wxmId);
        List<SysUser> sysUsers = selectList(params);
        if (sysUsers.size()>0) {
            return sysUsers.get(0);
        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateRoles(SysUser sysUser,String role[],List<SysUserDataScope> dataScopeList) throws Exception{
        Integer w = 1;
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setUserId(sysUser.getId());
        sysRoleUserService.delete(sysRoleUser);
        if (role != null) {
            for (String r : role) {
                w = w+1;
                sysRoleUser = new SysRoleUser();
                sysRoleUser.setId(CodeGenerator.getUUID());
                sysRoleUser.setRoleId(r);
                sysRoleUser.setUserId(sysUser.getId());
                sysRoleUserService.insert(sysRoleUser);
            }
        }
        return w;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysUser sysUser = selectByPrimaryKey(id);
            sysUser.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysUser);
        }
        return rw;
    }

}
