/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.core.entity.SysJob;
import com.rzico.core.service.SysJobService;
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
 * 计划任务表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-07
 */
@Api(description = "计划任务表接口")
@RestController
@RequestMapping("/admin/sysJob")
public class SysJobController extends BaseController {

    @Autowired
    private SysJobService sysJobService;

    /**
     * 查询分页记录
     *
     * @return
    */
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<SysJob> list(String startDate, String endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysJob> list = sysJobService.selectList(params);
        PageResult<SysJob> pageResult = new PageResult<SysJob>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysJob> find(@PathVariable String id) {
        SysJob result = sysJobService.selectByPrimaryKey(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysJob",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysJob
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")
    public CommResult<SysJob> save(@RequestBody SysJob sysJob) {
        if (sysJob.getExecStatus()!=null) {
            sysJob.setExecStatus(0);
        }
        int affectCount = sysJobService.insert(sysJob);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysJob result = sysJobService.findById(sysJob.getId());
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
    public CommResult<SysJob> del(@PathVariable String ids) {
        sysJobService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysJob
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysJob> update(@RequestBody SysJob sysJob) {
        if (sysJob.getExecStatus()!=null) {
            sysJob.setExecStatus(0);
        }
        int affectCount = sysJobService.updateByPrimaryKeySelective(sysJob);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysJob result = sysJobService.findById(sysJob.getId());
        return CommResult.success(result);
    }


}
