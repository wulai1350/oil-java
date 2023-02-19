package com.rzico.core.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysMchMenu;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysMchMenuService;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysMenuService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.exception.CustomException;
import com.rzico.util.MD5Utils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  商戶號控制层
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-12-15
*/
@Api(description="商戶號")
@RestController
@RequestMapping("/admin/sysMch")
public class SysMchController extends BaseController {

    @Autowired
    private SysMchService sysMchService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询分页记录
     * @param pageable
     * @return
     */
    @ApiOperation("查询分页记录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "商户名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "shortName", value = "商户简称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "mchType", value = "商户类型 (0.运营商 1.普通商户 2.特约商户 3.服务商户 4.代理商户)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "电话", dataType = "String", paramType = "query")
    })

    public CommResult<SysMch> list(String startDate, String endDate,
                                                String name, String shortName, Boolean delFlag,
                                                Integer status,Integer mchType, String phone, Pageable pageable) {

        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }
        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
        if (StringUtils.isNotEmpty(shortName)){
            params.put("shortName", shortName);
        }
        if (null != status){
            params.put("status", status);
        }
        if (null != mchType){
            params.put("mchType", mchType);
        }
        if (StringUtils.isNotEmpty(phone)){
            params.put("phone", phone);
        }
        if (null == delFlag){
            params.put("delFlag", false);
        }else{
            params.put("delFlag", delFlag);
        }
        SysUser sysUser = sysUserService.getCurrent();

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysMch> list = sysMchService.selectList(params);
        PageResult<SysMch> pageResult = new PageResult<SysMch>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 获取当前账号商户资料
     *
     * @return
     */
    @ApiOperation("获取当前账号商户资料")
    @GetMapping("/getInfo")
    public CommResult<SysMch> getCurrent() {
        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser.getMchId()!=null) {
            SysMch sysMch = sysMchService.selectByPrimaryKey(sysUser.getMchId());
            return CommResult.success(sysMch);
        } else {
            return CommResult.success(null);
        }
    }

    /**
     * 查询单条记录
     * @param id
     * @return
     */
    @ApiOperation("查询单条记录")
    @GetMapping(value = "/find/{id}")
    public CommResult<SysMch> find(@ApiParam(name="id",value="商户号id",required=true)@PathVariable String id) {
        SysMch result = sysMchService.selectByPrimaryKey(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysMch",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     * @param sysMch
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping(value = "/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名（手机/邮箱）", required=false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "验证码", required=false, dataType = "String", paramType = "query")
    })
    public CommResult<SysMch> save(@RequestBody SysMch sysMch,String username,String password) {

        Assert.notNull(sysMch, "参数为空");
        Assert.notNull(sysMch.getName(), "名称为空");
        Assert.notNull(sysMch.getShortName(), "简称为空");
        Assert.notNull(sysMch.getAreaId(), "所在地为空");
        Assert.notNull(sysMch.getAreaName(), "所在地为空");

        try {
            SysUser sysUser = sysUserService.getCurrent();

            if (sysUser.getMchId()!=null) {

                SysMch curMch = sysMchService.selectByPrimaryKey(sysUser.getMchId());

            }

            int affectCount = sysMchService.save(sysMch, username, password);
            if (affectCount <= 0){
                return CommResult.error();
            }

        } catch (CustomException e) {
            return CommResult.error(e.getMessage());
        }
        SysMch result = sysMchService.findById(sysMch.getId());
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
    public CommResult<SysMch> del(@PathVariable String ids) {
        sysMchService.deleteByIds(ids.split(","));
        return CommResult.success();
    }


    /**
     * 首次登录初始化数据
     *
     * @return
     */
    @Log(desc = "初始化数据", type = Log.LOG_TYPE.DEL)
    @ApiOperation("首次登录初始化数据")
    @PostMapping("/initData")
    public CommResult<SysMch> initData() {
        SysUser sysUser = sysUserService.getCurrent();
        SysMch sysMch = sysMchService.findById(sysUser.getMchId());
        sysMchService.initData(sysMch);
        return CommResult.success();
    }


    /**
     * 更新记录
     *
     * @param sysMch
     * @return
     */
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysMch> update(@RequestBody SysMch sysMch) {

        int affectCount = sysMchService.updateByPrimaryKeySelective(sysMch);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysMch result = sysMchService.findById(sysMch.getId());
        return CommResult.success(result);
    }

    /**
     * 商户已授权菜单
     *
     * @param id
     * @return
     */
    @ApiOperation("商户已授权菜单")
    @PostMapping("/getMenus")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商户id", dataType = "String", allowMultiple = true, paramType = "query")})
    public CommResult<SysMchMenu> getMenus(String id) {
        Assert.notNull(id, "商户号为空");

        SysMch sysMch = sysMchService.findById(id);

        List<SysMenu> menuList = sysMch.getMenuList();

        return CommResult.success(menuList);

    }

    private Boolean checkMenuExists(List<SysMenu> menus,SysMenu menu,SysMch sysMch) {
        Boolean finded = false;
        for (SysMenu m:menus) {
            if (m.getId().equals(menu.getId())) {
                finded = true;
                break;
            }
        }

        return finded;
    }

    /**
     * 商户已授权菜单
     *
     * @return
     */
    @ApiOperation("商户已授权菜单（树）")
    @GetMapping(value = "/getMenuTree")
    public CommResult<SysMenu> getMenuTree() {
        SysUser sysUser = sysUserService.getCurrent();
        SysMch sysMch = sysMchService.findById(sysUser.getMchId());
        List<SysMenu> meuns = sysMch.getMenuList();
        List<SysMenu> list = sysMenuService.treeList(1, null);
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

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @ApiOperation("重置密码")
    @PostMapping("/resetPwd")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商户id", dataType = "String", allowMultiple = true, paramType = "query")})
    public CommResult<SysMchMenu> resetPwd(String id) {
        Assert.notNull(id, "商户号为空");

        SysMch sysMch = sysMchService.findById(id);

        SysUser sysUser = sysUserService.findById(sysMch.getAdminId());
        if (sysUser==null) {
            return CommResult.error("没有设置管理员");
        }

        String password = MD5Utils.getMD5Str("123456");
        sysUser.setPassword(MD5Utils.getMD5Str(password.trim() + sysUser.getUsername().trim()));

        sysUserService.update(sysUser);

        return CommResult.success();

    }

}
