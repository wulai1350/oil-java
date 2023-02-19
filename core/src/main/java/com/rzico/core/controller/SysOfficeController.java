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
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.util.CodeGenerator;
import com.rzico.util.StringUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  组织机构制层
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-12-15
 *
 */
@Api(description="组织机构")
@RestController
@RequestMapping("/admin/sysOffice")
public class SysOfficeController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SysOfficeService sysOfficeService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMchService sysMchService;


    @ApiOperation("查询记录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "officeId", value = "公司、部门编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "scoped", value = "启用数据权限", dataType = "Boolean", paramType = "query")})
    public CommResult<SysOffice> tree(String officeId,Boolean scoped) throws Exception {

        SysUser sysUser = sysUserService.getCurrent();
        Map<String, Object> params = new HashMap<String, Object>();

        SysMch sysMch = sysMchService.getCurrent();
        params.put("mchId", sysMch.getId());

        if (StringUtils.isNotEmpty(officeId)){
            params.put("treePath", officeId);
        }
//        if (!sysUserService.isAdmin(sysUser)){
//            if (null == scoped || scoped==false) {
//                params.put("dataScopeId",sysUser.getId());
//            }
//        }
        params.put("delFlag", false);
        params.put("sortType", "ASC");
        params.put("sortField", "tree_path");

        List list = sysOfficeService.selectTree(params);
        return CommResult.success(list);
    }


    /**
     * 查询单条记录
     *
     * @return
     */
    @ApiOperation("查询单条记录")
    @GetMapping(value = "/find/{id}")
    public CommResult<SysOffice> find(@PathVariable String id) {
        SysOffice result = sysOfficeService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysOffice",result);
        return CommResult.success(data);
    }


    @ApiOperation("保存记录")
    @PostMapping(value = "/save")
    public CommResult<SysOffice> save(@RequestBody SysOffice sysOffice) {
        logger.info("保存office参数: [{}]", sysOffice);
        Assert.notNull(sysOffice, "参数为空");
        Assert.notNull(sysOffice.getName(), "名称为空");


        SysMch sysMch = sysMchService.getCurrent();
        sysOffice.setMchId(sysMch.getId());

        if (null == sysOffice.getDelFlag()){
            sysOffice.setDelFlag(false);
        }
        String id = CodeGenerator.getUUID();
        sysOffice.setId(id);
        sysOffice.setDataSort(0);
        int affectCount = sysOfficeService.insert(sysOffice);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysOffice result = sysOfficeService.findById(id);
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
    public CommResult<SysOffice> del(@PathVariable String ids) {
        sysOfficeService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysOffice
     * @return
     */
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysOffice> update(@RequestBody SysOffice sysOffice) {
        Assert.notNull(sysOffice.getId(), "officeId为空");
        if (StringUtils.isEmpty(sysOffice.getMchId())){
            SysMch sysMch = sysMchService.getCurrent();
            sysOffice.setMchId(sysMch.getId());
        }

        int affectCount = sysOfficeService.updateByPrimaryKeySelective(sysOffice);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysOffice result = sysOfficeService.findById(sysOffice.getId());
        return CommResult.success(result);
    }

}
