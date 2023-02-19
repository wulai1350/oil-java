package com.rzico.core.service;

import com.alibaba.fastjson.JSON;
import com.rzico.base.BaseMapper;
import com.rzico.base.CommResult;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.*;
import com.rzico.core.mapper.SysMchMapper;
import com.rzico.core.mapper.SysMchMenuMapper;
import com.rzico.core.mapper.SysMenuMapper;
import com.rzico.exception.CustomException;
import com.rzico.util.*;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
*
<pre>
 * 系统角色业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysMchService extends BaseServiceImpl<SysMch, String> {

    @Autowired
    private SysMchMapper sysMchMapper;

    @Autowired @Lazy
    private SysMchMenuMapper sysMchMenuMapper;

    @Autowired @Lazy
    private SysMenuMapper sysMenuMapper;

    @Autowired @Lazy
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysSequenceService sysSequenceService;

    @Autowired
    private SysOfficeService sysOfficeService;

    @Autowired
    private SysEmployeeService sysEmployeeService;

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public BaseMapper<SysMch, String> getMapper() {
        return sysMchMapper;
    }



    public SysMch getCurrent() {
        Map<String,String> sysUser = super.getToken();
        if (sysUser!=null) {
            if (StringUtils.isNotEmpty(sysUser.get("hMchId"))) {
                sysUser.put("mchId",sysUser.get("hMchId"));
            } else {
                SysUser user = sysUserService.selectByPrimaryKey(sysUser.get("id"));
                sysUser.put("mchId",user.getMchId());
            }
            return selectByPrimaryKey(sysUser.get("mchId"));
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(SysMch sysMch,String username,String password) {

        //1、新增商户信息
        String mchId = String.valueOf(sysSequenceService.generateNum("mch")+10200L);
        sysMch.setId(mchId);
        sysMch.setCreateDate(new Date());
        sysMch.setStatus(0);
        sysMch.setDelFlag(false);

        SysUser sysUser = sysUserService.getCurrent();

        //2、新增登录账号
        if (sysUser!=null && sysUser.getMchId() == null) {
            SysUser user = new SysUser();
            user.setId(sysUser.getId());
            user.setMchId(mchId);
            sysUserService.updateByPrimaryKeySelective(user);
            sysMch.setAdminId(sysUser.getId());
        } else
        if (username!=null) {
            //后台添加商户
            int result = sysUserService.checkUserExists(username);
            if (result > 0) {
                throw new CustomException("用户名已存在");
            }

            SysUser user = new SysUser();
            String userId = CodeGenerator.getUUID();
            user.setId(userId);
            user.setMchId(mchId);
            user.setUsername(username);
            if (username.indexOf("@") < 0) {
                user.setMobile(username);
            }
            //设置默认密码
            if (password==null) {
                password = MD5Utils.getMD5Str(username);
            }
            user.setPassword(MD5Utils.getMD5Str(password + username.trim()));
            user.setNickname((null == sysMch.getShortName() || sysMch.getShortName().equals("")) ? sysMch.getName() : sysMch.getShortName());
            user.setStatus(SysUser.STATUS_ENABLED);
            user.setUserType(1);
            user.setDelFlag(false);
            sysMch.setAdminId(user.getId());
            int num = sysUserService.insert(user);
            if (num == 0) {
                throw new CustomException("用户注册失败");
            }

            //3、新增员工
            SysEmployee sysEmployee = new SysEmployee();
            sysEmployee.setId(CodeGenerator.getUUID());
            sysEmployee.setMchId(mchId);
            sysEmployee.setName((null == sysMch.getShortName() || sysMch.getShortName().equals("")) ? sysMch.getName() : sysMch.getShortName());
            sysEmployee.setStatus(1);
            sysEmployee.setUserId(userId);
            sysEmployee.setDelFlag(false);
            sysEmployee.setEmpNo(username);
            sysEmployeeService.insert(sysEmployee);

        } else {
            throw new CustomException("用户名称不能为空");
        }
        int affectCount = this.insert(sysMch);
        if (affectCount == 0) {
            throw new CustomException("保存失败");
        }
        return affectCount;
    }

    @Transactional(rollbackFor = Exception.class)
    public int initData(SysMch sysMch) {
        int affectCount = 0;
        SysUser sysUser = sysUserService.findByUsername("admin");
        //初始化角色
        SysRole sysRole = new SysRole();
        sysRole.setMchId(sysUser.getMchId());
        List<SysRole> sysRoles = sysRoleService.select(sysRole);
        for (SysRole r:sysRoles) {
            r.setMchId(sysMch.getId());
            r.setId(CodeGenerator.getUUID());
            sysRoleService.insert(r);
        }
        //初始化组织架构
        SysOffice sysOffice = new SysOffice();
        sysOffice.setId(sysMch.getId());
        if (sysMch.getAreaName()!=null) {
            sysOffice.setAddress(sysMch.getAreaName().concat(sysMch.getAddress()));
        }
        sysOffice.setName(sysMch.getName());
        sysOffice.setDelFlag(false);
        sysOffice.setPhone(sysMch.getPhone());
        sysOffice.setLeader(sysMch.getLinkman());
        sysOffice.setTreePath(","+sysOffice.getId()+",");
        sysOffice.setDataSort(0);
        sysOffice.setMchId(sysMch.getId());
        sysOffice.setType(0);
        sysOffice.setStatus(1);
        sysOfficeService.insert(sysOffice);
        //初始化员工
        SysEmployee sysEmployee = new SysEmployee();
        sysEmployee.setId(CodeGenerator.getUUID());
        sysEmployee.setName(sysMch.getLinkman());
        sysEmployee.setDelFlag(false);
        sysEmployee.setUserId(sysMch.getAdminId());
        sysEmployee.setEmpNo(sysMch.getPhone());
        sysEmployee.setOfficeId(sysOffice.getId());
        sysEmployee.setMchId(sysMch.getId());
        sysEmployee.setStatus(1);
        sysEmployeeService.insert(sysEmployee);
        //初始化其他(通过MQ发送请求)

        //更新初始化状态
        sysMch.setStatus(SysMch.STATUS_ENABLED);
        sysMchMapper.updateByPrimaryKeySelective(sysMch);
        return affectCount;
    }

    public SysMch findByUserId(String userId) {
        Map<String,Object> params = new HashMap<>();
        params.put("adminId",userId);
        List<SysMch> sysMchList = selectList(params);
        if (sysMchList.size()>0) {
            return  sysMchList.get(0);
        } else {
            return null;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysMch sysMch = selectByPrimaryKey(id);
            sysMch.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysMch);
        }
        return rw;
    }

}
