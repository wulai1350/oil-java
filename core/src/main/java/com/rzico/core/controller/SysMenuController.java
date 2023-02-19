package com.rzico.core.controller;

import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysMenuService;
import com.rzico.core.service.SysUserService;
import com.rzico.util.CodeGenerator;
import com.rzico.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
*
<pre>
 * 系统菜单控制层
 * </pre>
*
* @author Rzico Boot
* @version 1.0
*/
@RestController
@RequestMapping("/admin/sysMenu")
@Api(value = "菜单管理",description="菜单业务处理")
public class SysMenuController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMchService sysMchService;

    @Autowired
    private SysUserService sysUserService;

    private Boolean checkMenuExists(List<SysMenu> menus,SysMenu menu,SysMch sysMch) {
        Boolean finded = false;
        for (SysMenu m:menus) {
            if (m.getId().equals(menu.getId())) {
                finded = true;
                break;
            }
        }
        if ("system-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysMch-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysMenu-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysIcon-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysLog-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysReport-list".equals(menu.getPermission())) {
            finded = false;
        }
        if ("sysDict-list".equals(menu.getPermission())) {
            finded = false;
        }
        return finded;
    }

    /**
     * 查询分页记录
     *
     * @return
     */
    @ApiOperation("查询树形记录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "menuType", value = "菜单类型(0菜单 1菜单和按钮)", dataType = "Integer", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "menuId", value = "菜单编号", dataType = "String", paramType = "query")})
    public CommResult<SysMenu> list(Integer menuType, String menuId) {

        if (null == menuType){
            menuType = 1;
        }

        SysUser sysUser = sysUserService.getCurrent();

        List<SysMenu> list = sysMenuService.treeList(menuType, menuId);
        SysMch sysMch = sysMchService.findById(sysUser.getMchId());
        List<SysMenu> meuns = sysMch.getMenuList();

        if (!"admin".equals(sysUser.getUsername())) {
            List<SysMenu> data = new ArrayList<>();
            for (SysMenu menu:list) {
                if (checkMenuExists(meuns,menu,sysMch)) {
                    data.add(menu);
                    List<SysMenu> childrens = new ArrayList<>();
                    for (SysMenu child:menu.getChildrens()) {
                        if (checkMenuExists(meuns,child,sysMch)) {
                            childrens.add(child);
                        }
                    }
                    menu.setChildrens(childrens);
                }
            }
            return CommResult.success(data);
        }

        return CommResult.success(list);

    }

    /**
     * 查询单条记录
     *
     * @return
     */
    @ApiOperation("查询单条记录")
    @GetMapping(value = "/find/{id}")
    public CommResult<SysMenu> find(@PathVariable String id) {
        SysMenu result = sysMenuService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysMenu",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysMenu
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping(value = "/save")
    public CommResult<SysMenu> save(@RequestBody SysMenu sysMenu,HttpServletRequest request) {
        Assert.notNull(sysMenu,"获取数据失败");
        Assert.notNull(sysMenu.getName(), "菜单名称为空");
        logger.info("菜单保存传参: [{}]", sysMenu.toString());
        sysMenu.setId(CodeGenerator.getUUID());
        sysMenu.setCreateDate(new Date());
        int affectCount = sysMenuService.insert(sysMenu);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysMenu result = sysMenuService.findById(sysMenu.getId());
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
    public CommResult<SysMenu> del(@PathVariable String ids) {
        sysMenuService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysMenu
     * @return
     */
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysMenu> update(@RequestBody SysMenu sysMenu) {

        int affectCount = sysMenuService.updateByPrimaryKeySelective(sysMenu);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysMenu result = sysMenuService.findById(sysMenu.getId());
        return CommResult.success(result);
    }



}
