package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.CommResult;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysRole;
import com.rzico.core.entity.SysRoleMenu;
import com.rzico.core.entity.SysRoleUser;
import com.rzico.core.mapper.SysRoleMapper;
import com.rzico.core.mapper.SysRoleUserMapper;
import com.rzico.exception.CustomException;
import com.rzico.util.BeanUtil;
import com.rzico.util.CodeGenerator;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
public class SysRoleService extends BaseServiceImpl<SysRole, String> {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public BaseMapper<SysRole, String> getMapper() {
        return sysRoleMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public int addRole(SysRole sysRole) {
        int rw = insert(sysRole);
        //操作role-menu data

        for (String menuId : sysRole.getMenus()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setId(CodeGenerator.getUUID());
            sysRoleMenu.setRoleId(sysRole.getId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuService.insert(sysRoleMenu);
        }

        return rw;
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole sysRole){
        int rw = updateByPrimaryKeySelective(sysRole);

            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());
            sysRoleMenuService.delete(sysRoleMenu);
            for (String menuId : sysRole.getMenus()) {
                sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setId(CodeGenerator.getUUID());
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenuService.insert(sysRoleMenu);
            }

        return rw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUser.setRoleId(String.valueOf(id));
            int count = sysRoleUserMapper.selectCount(sysRoleUser);
            if (count > 0) {
                throw new CustomException("已分配给用户，删除失败");
            }

            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(String.valueOf(id));
            sysRoleMenuService.delete(sysRoleMenu);
            int affectCount = deleteByPrimaryKey(id);
            if (affectCount == 0) {
                throw new CustomException("无效角色ID"+id);
            }
            rw = rw + affectCount;
        }
        return rw;
    }
}
