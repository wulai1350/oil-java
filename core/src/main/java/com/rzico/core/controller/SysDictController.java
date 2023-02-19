/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-16
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
import com.rzico.core.entity.SysDict;
import com.rzico.core.service.SysDictService;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;

import java.util.*;

/**
 * 数据字典控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-04-16
 */
@Api(description = "数据字典接口")
@RestController
@RequestMapping("/admin/sysDict")
public class SysDictController extends BaseController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 分页查询数据字典
     *
     * @return
    */
    @ApiOperation("分页查询数据字典")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "name", value = "字典名称", dataType = "String", paramType = "query")
    })
    public CommResult<SysDict> list(String dictType, String name, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(dictType)){
            params.put("dictType", dictType);
        }
        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
        params.put("delFlag", false);

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysDict> list = sysDictService.selectList(params);
        PageResult<SysDict> pageResult = new PageResult<SysDict>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条数据字典
     *
     * @return
    */
    @ApiOperation("查询单条数据字典")
    @GetMapping("/find/{id}")
    public CommResult<SysDict> find(@PathVariable String id) {
        SysDict result = sysDictService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("sysDict",result);
        return CommResult.success(data);

    }

    /**
     * 保存数据字典
     *
     * @param sysDict
     * @return
     */
    @ApiOperation("保存数据字典")
    @PostMapping("/save")
    public CommResult<SysDict> save(@RequestBody SysDict sysDict) {
        sysDict.setCreateDate(new Date());
        sysDict.setId(CodeGenerator.getUUID());
        sysDict.setDelFlag(false);
        int affectCount = sysDictService.insert(sysDict);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysDict result = sysDictService.findById(sysDict.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除数据字典
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除数据字典", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除数据字典,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysDict> del(@PathVariable String ids) {

        sysDictService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新数据字典
     *
     * @param sysDict
     * @return
     */
    @Log(desc = "更新数据字典", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新数据字典")
    @PostMapping("/update")
    public CommResult<SysDict> update(@RequestBody SysDict sysDict) {
        int affectCount = sysDictService.updateByPrimaryKeySelective(sysDict);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysDict result = sysDictService.findById(sysDict.getId());
        return CommResult.success(result);
    }

}
