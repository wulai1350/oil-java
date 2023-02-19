/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysOffice;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysOfficeService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.core.entity.SysEmployee;
import com.rzico.core.service.SysEmployeeService;
import com.rzico.util.CodeGenerator;
import com.rzico.util.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-01-11
 */
@Api(description = "人员表接口")
@RestController
@RequestMapping("/admin/sysEmployee")
public class SysEmployeeController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysEmployeeService sysEmployeeService;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SysMchService sysMchService;

    /**
     * 查询分页记录
     *
     * @return
    */
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "empNo", value = "员工工号", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "name", value = "员工名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "officeId", value = "部门编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "shopId", value = "门店名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "scoped", value = "启用数据权限", dataType = "Boolean", paramType = "query")
    })
    public CommResult<SysEmployee> list(String empNo, String name, String officeId,Long shopId,Integer employeeType,Boolean scoped, Pageable pageable) {
        CommResult<SysEmployee> commResult = new CommResult<SysEmployee>();
        Map<String, Object> params = new HashMap<String, Object>();
        SysUser sysUser = sysUserService.getCurrent();

        SysMch sysMch = sysMchService.getCurrent();
        params.put("mchId", sysMch.getId());

        if (StringUtils.isNotEmpty(empNo)){
            params.put("empNo", empNo);
        }
        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
//        if (null == scoped || scoped==false) {
//            if (!sysUserService.isAdmin(sysUser)) {
//                params.put("dataScopeId", sysUser.getId());
//            }
//        }
        if (StringUtils.isNotEmpty(officeId)){
            params.put("officeId", officeId);
        }
        if (shopId!=null){
            params.put("shopId", shopId);
        }
        params.put("delFlag", false);
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysEmployee> list = sysEmployeeService.selectList(params);
        PageResult<SysEmployee> pageResult = new PageResult<SysEmployee>(list, startPage.getTotal(), pageable);
        commResult.setData(pageResult);
        return commResult;
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysEmployee> find(@PathVariable String id) {

        Map<String,Object> data = new HashMap<>();
        SysEmployee result = sysEmployeeService.findById(id);
        data.put("sysEmployee",result);
        data.put("postList",result.getPostList());

        if (result.getUserId()!=null) {
            SysUser sysUser = sysUserService.findById(result.getUserId());
            data.put("sysUser", sysUser);
        }

        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysEmployee
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postIds", value = "岗位ID(数组ids)", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名（手机/邮箱）", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "验证码", required=false, dataType = "String", paramType = "query")
    })
    public CommResult<SysEmployee> save(@RequestBody SysEmployee sysEmployee,String postIds[],String username,String password) {
        if (null == sysEmployee.getDelFlag()){
            sysEmployee.setDelFlag(false);
        }

        SysMch sysMch = sysMchService.getCurrent();

        sysEmployee.setMchId(sysMch.getId());
        int affectCount = sysEmployeeService.save(sysEmployee,username,password,postIds);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysEmployee result = sysEmployeeService.findById(sysEmployee.getId());
        return CommResult.success(sysEmployee);
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
    public CommResult<SysEmployee> del(@PathVariable String ids) {
        sysEmployeeService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysEmployee
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysEmployee> update(@RequestBody SysEmployee sysEmployee,String postIds[]) {


        int affectCount = sysEmployeeService.update(sysEmployee,postIds);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysEmployee result = sysEmployeeService.findById(sysEmployee.getId());
        return CommResult.success(result);
    }

}
