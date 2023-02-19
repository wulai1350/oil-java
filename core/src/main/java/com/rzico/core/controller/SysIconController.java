/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-05
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
import com.rzico.core.entity.SysIcon;
import com.rzico.core.service.SysIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;

import java.util.*;

/**
 * 设计器控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-03-05
 */
@Api(description = "设计器接口")
@RestController
@RequestMapping("/admin/sysIcon")
public class SysIconController extends BaseController {

    @Autowired
    private SysIconService sysIconService;

    /**
     * 分页查询设计器
     *
     * @return
    */
    @ApiOperation("分页查询设计器")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iconType", value = "类型(0:图标，1:素材)", dataType = "Integer", paramType = "query")
    })
    public CommResult<SysIcon> list(String name, Integer iconType, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);

        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
        if (iconType!=null){
            params.put("iconType", iconType);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysIcon> list = sysIconService.selectList(params);
        PageResult<SysIcon> pageResult = new PageResult<SysIcon>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 保存设计器
     *
     * @param sysIcon
     * @return
     */
    @ApiOperation("保存设计器")
    @PostMapping("/save")
    public CommResult<SysIcon> save(@RequestBody SysIcon sysIcon) {
        sysIcon.setCreateDate(new Date());
        int affectCount = sysIconService.insert(sysIcon);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysIcon result = sysIconService.findById(sysIcon.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除设计器
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除设计器", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除设计器,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysIcon> del(@PathVariable String ids) {

        sysIconService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

}
