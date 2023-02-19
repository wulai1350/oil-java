/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.CommResult;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.*;
import com.rzico.exception.CustomException;
import com.rzico.util.CodeGenerator;
import com.rzico.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysEmployeeMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 人员表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysEmployeeService extends BaseServiceImpl<SysEmployee, String> {

    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;

    @Autowired @Lazy
    private SysUserService sysUserService;

    @Autowired
    private SysEmployeePostService sysEmployeePostService;

    @Override
    public BaseMapper<SysEmployee, String> getMapper() {
        return sysEmployeeMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(SysEmployee sysEmployee, String username, String password,String postIds[]) {

        String id = CodeGenerator.getUUID();
        Boolean hasHistory = false;
        Boolean hasSysUser = false;
        String userId = null;
        if (username!=null) {
            SysUser checkUser = sysUserService.findByUsername(username);
            if (checkUser != null) {
                if (checkUser.getMchId() != null) {
                    if (sysEmployee.getMchId().equals(checkUser.getMchId())) {
                        SysEmployee emp = findByUserId(checkUser.getId());
                        if (emp!=null && !emp.getDelFlag()) {
                            throw new CustomException("员工已存在");
                        } else {
                            if (emp!=null) {
                                hasHistory = true;
                                id = emp.getId();
                            }
                        }
                        userId = checkUser.getId();
                        hasSysUser = true;
                    } else {
                        throw new CustomException("用户名已存在");
                    }
                } else {
                    //有用户没绑定商户的
                    userId = checkUser.getId();
                    hasSysUser = true;
                }
            }
        }

        sysEmployee.setId(id);
        sysEmployee.setDelFlag(false);
        if (username!=null) {
            if (userId==null) {
                userId = CodeGenerator.getUUID();
            }
            sysEmployee.setUserId(userId);
        }

        int affectCount = 0;
        if (hasHistory) {
            affectCount = super.updateByPrimaryKeySelective(sysEmployee);
        } else {
            affectCount = super.insert(sysEmployee);
        }
        if (affectCount <= 0) {
            throw new CustomException("更新失败");
        }

        if (postIds != null) {
            for (String r : postIds) {
                SysEmployeePost employeePost = new SysEmployeePost();
                employeePost.setId(CodeGenerator.getUUID());
                employeePost.setEmployeeId(sysEmployee.getId());
                employeePost.setPostId(r);
                sysEmployeePostService.insert(employeePost);
            }
        }

        if (username!=null && hasSysUser==false) {
            //后台添加商户
            SysUser sysUser = new SysUser();
            sysUser.setId(userId);
            sysUser.setMchId(sysEmployee.getMchId());
            sysUser.setUsername(username);
            if (username.indexOf("@") < 0 && username.length()==11) {
                sysUser.setMobile(username);
            }
            //设置默认密码
            if (password==null) {
                password = MD5Utils.getMD5Str(username);
            }
            sysUser.setPassword(MD5Utils.getMD5Str(password + username.trim()));
            sysUser.setNickname(sysEmployee.getName());
            sysUser.setStatus(SysUser.STATUS_ENABLED);
            sysUser.setUserType(1);
            sysUser.setDelFlag(false);
            int num = sysUserService.insert(sysUser);
            if (num == 0) {
                throw new CustomException("用户注册失败");
            }

        }
        return affectCount;
    }



    @Transactional(rollbackFor = Exception.class)
    public int update(SysEmployee sysEmployee,String postIds[]) {
        int affectCount = sysEmployeeMapper.updateByPrimaryKeySelective(sysEmployee);
        if (affectCount <= 0){
            throw new CustomException("无效员工id");
        }

        SysEmployeePost sysEmployeePost = new SysEmployeePost();
        sysEmployeePost.setEmployeeId(sysEmployee.getId());
        sysEmployeePostService.delete(sysEmployeePost);

        if (postIds != null) {
            for (String r : postIds) {
                SysEmployeePost employeePost = new SysEmployeePost();
                employeePost.setId(CodeGenerator.getUUID());
                employeePost.setEmployeeId(sysEmployee.getId());
                employeePost.setPostId(r);
                sysEmployeePostService.insert(employeePost);
            }
        }

        return affectCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysEmployee sysEmployee = selectByPrimaryKey(id);

            SysUser sysUser = sysUserService.selectByPrimaryKey(sysEmployee.getUserId());
            if (sysUserService.isAdmin(sysUser)) {
               throw new CustomException("店主号不能删除");
            }

            sysEmployee.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysEmployee);


            if (sysUser!=null) {
                sysUser.setMchId(null);
                sysUserService.updateByPrimaryKey(sysUser);
            }
        }
        return rw;
    }

    public SysEmployee findByUserId(String userId) {
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        List<SysEmployee> sysEmployeeList = super.selectList(params);
        if (sysEmployeeList.size()>0) {
            return sysEmployeeList.get(0);
        } else {
            return null;
        }
    }

    public SysEmployee getCurrent() {
        Map<String,String> sysUser = super.getToken();
        if (sysUser!=null) {
            return findByUserId(sysUser.get("id"));
        }
        return null;

    }
}
