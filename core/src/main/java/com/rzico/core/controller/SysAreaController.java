/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-20
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
import com.rzico.core.entity.SysArea;
import com.rzico.core.service.SysAreaService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-01-20
 */
@Api(description = "区域管理接口")
@RestController
@RequestMapping("/admin/sysArea")
public class SysAreaController extends BaseController {

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 查询树形结构
     *
     * @return
     */
    @ApiOperation("树形结构查询行政区")
    @GetMapping("/list")
    public CommResult<SysArea> treeList(Pageable pageable) {
        List<SysArea> list = null;
        try {
            list = sysAreaService.selectTree();
        } catch (Exception e){
            e.printStackTrace();
            return CommResult.error(e.getMessage());
        }
        return CommResult.success(list);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条行政区")
    @GetMapping("/find/{id}")
    public CommResult<SysArea> find(@PathVariable String id) {

        SysArea result = sysAreaService.selectByPrimaryKey(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysArea",result);
        return CommResult.success(data);

    }

    /**
     * 保存记录
     *
     * @param sysArea
     * @return
     */
    @ApiOperation("保存行政区")
    @PostMapping("/save")
    public CommResult<SysArea> save(@RequestBody SysArea sysArea) {
        int affectCount = sysAreaService.insert(sysArea);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysArea result = sysAreaService.findById(sysArea.getId());
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
    public CommResult<SysArea> del(@PathVariable String ids) {
        CommResult<SysArea> commResult = new CommResult<SysArea>();
        sysAreaService.deleteByIds(ids.split(","));
        return commResult;
    }

    /**
     * 更新记录
     *
     * @param sysArea
     * @return
     */
    @Log(desc = "更新行政区", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新行政区")
    @PostMapping("/update")
    public CommResult<SysArea> update(@RequestBody SysArea sysArea) {
        int affectCount = sysAreaService.updateByPrimaryKeySelective(sysArea);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysArea result = sysAreaService.findById(sysArea.getId());
        return CommResult.success(result);
    }

}
