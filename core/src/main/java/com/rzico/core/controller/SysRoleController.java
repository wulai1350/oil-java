package com.rzico.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysRole;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.*;
import com.rzico.entity.Filter;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.exception.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
<pre>
 * 系统角色控制层
 * </pre>
*
* @author Rzico Boot
* @version 1.0
*/
@RestController
@Api(description = "系统角色")
@RequestMapping("/admin/sysRole")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMchService sysMchService;


    @ApiOperation("查询记分页录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "角色名称", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query")
    })
    public CommResult list(String name, Integer status, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);

        SysMch sysMch = sysMchService.getCurrent();
        params.put("mchId", sysMch.getId());


        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
        if (null != status){
            params.put("status", status);
        }
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysRole> list = sysRoleService.selectList(params);
        PageResult<SysRole> pageResult = new PageResult<SysRole>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    @ApiOperation("查询角色及菜单权限")
    @GetMapping(value = "/find/{id}")
    public CommResult<SysRole> find(@PathVariable String id) {
        Map<String, Object> data = new HashMap<>();
        SysRole sysRole = sysRoleService.findById(id);
        data.put("sysRole", sysRole);
        data.put("sysMenu", sysRole.getMenuList());
        return CommResult.success(data);
    }


    @ApiOperation("保存角色及菜单权限")
    @PostMapping(value = "/save")
    @ApiImplicitParams({@ApiImplicitParam(name = "menus", value = "菜单数组", dataType = "string", allowMultiple=true, paramType = "query")})
    public CommResult<SysRole> save(@RequestBody SysRole sysRole) {
        Assert.notNull(sysRole, "参数为空");
        Assert.notNull(sysRole.getName(), "角色名称为空");

        SysMch sysMch = sysMchService.getCurrent();

        sysRole.setMchId(sysMch.getId());

        if (null == sysRole.getStatus()){
            sysRole.setStatus(1);
        }
        int affectCount = sysRoleService.addRole(sysRole);
        if (affectCount == 0) {
            return CommResult.error();
        }
        SysRole result = sysRoleService.findById(sysRole.getId());
        return CommResult.success(result);
    }

    /**
     * 更新不为空字段
     *
     * @param sysRole
     * @return
     */
    @ApiOperation("更新角色及菜单权限")
    @Log(desc = "更新角色及菜单权限", type = Log.LOG_TYPE.UPDATE)
    @PostMapping("/update")
    public CommResult<SysRole> update(@RequestBody SysRole sysRole) {
        Assert.notNull(sysRole, "参数为空");
        Assert.notNull(sysRole.getId(), "角色ID为空");
        int affectCount = sysRoleService.updateRole(sysRole);
        if (affectCount == 0) {
            return CommResult.error();
        }
        SysRole result = sysRoleService.findById(sysRole.getId());
        return CommResult.success(result);
    }


    /**
     * 更新不为空字段
     *
     * @param sysRole
     * @return
     */
    @ApiOperation("更新角色及菜单权限")
    @Log(desc = "更新角色及菜单权限", type = Log.LOG_TYPE.UPDATE)
    @PostMapping("/updateRole")
    public CommResult<SysRole> updateRole(@RequestBody SysRole sysRole) {
        Assert.notNull(sysRole, "参数为空");
        Assert.notNull(sysRole.getId(), "角色ID为空");
        int affectCount = sysRoleService.updateByPrimaryKeySelective(sysRole);
        if (affectCount == 0) {
            return CommResult.error();
        }
        SysRole result = sysRoleService.findById(sysRole.getId());
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
    public CommResult<SysRole> del(@PathVariable String ids) {
        try {
            int affectCount = sysRoleService.deleteByIds(ids.split(","));
            if (affectCount == 0) {
                return CommResult.error();
            }
            return CommResult.success("删除成功");

        } catch (CustomException e) {
            return CommResult.error(e.getMessage());
        }
        catch (Exception e) {
            return CommResult.error();
        }
     }


}
