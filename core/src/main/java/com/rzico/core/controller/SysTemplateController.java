/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.core.entity.SysTemplate;
import com.rzico.core.service.SysTemplateService;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模板
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-09
 */
@Api(description = "消息模板")
@RestController
@RequestMapping("/admin/sysTemplate")
public class SysTemplateController extends BaseController {

    @Autowired
    private SysTemplateService sysTemplateService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询分页记录
     *
     * @return
    */
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tplName", value = "模板名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tplKey", value = "模板Key", dataType = "String", paramType = "query")
    })
    public CommResult<SysTemplate> list(String startDate, String endDate,String tplName,String tplKey, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }
        SysUser sysUser = sysUserService.getCurrent();

        params.put("mchId", sysUser.getMchId());

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(tplName)){
            params.put("tplName", tplName);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(tplKey)){
            params.put("tplKey", tplKey);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysTemplate> list = sysTemplateService.selectList(params);
        PageResult<SysTemplate> pageResult = new PageResult<SysTemplate>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysTemplate> find(@PathVariable String id) {
        SysTemplate result = sysTemplateService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysTemplate",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysTemplate
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")

    public CommResult<SysTemplate> save(@RequestBody SysTemplate sysTemplate) {

        SysUser sysUser = sysUserService.getCurrent();
        sysTemplate.setMchId(sysUser.getMchId());
        sysTemplate.setId(CodeGenerator.getUUID());
        int affectCount = sysTemplateService.insert(sysTemplate);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysTemplate result = sysTemplateService.findById(sysTemplate.getId());
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
    public CommResult<SysTemplate> del(@PathVariable String ids) {
        sysTemplateService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysTemplate
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysTemplate> update(@RequestBody SysTemplate sysTemplate) {
        int affectCount = sysTemplateService.updateByPrimaryKeySelective(sysTemplate);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysTemplate result = sysTemplateService.findById(sysTemplate.getId());
        return CommResult.success(result);
    }

}
