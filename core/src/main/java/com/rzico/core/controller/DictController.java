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
import com.rzico.core.entity.SysDict;
import com.rzico.core.service.SysDictService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.util.CodeGenerator;
import com.rzico.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-04-16
 */
@Api(description = "前端数据字典接口")
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 分页查询数据字典
     *
     * @return
    */
    @ApiOperation("分页查询数据字典")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", paramType = "query")
    })
    public CommResult<SysDict> list(String dictType, String name, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(dictType)){
            params.put("dictType", dictType);
        }

        params.put("delFlag", false);

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysDict> list = sysDictService.selectList(params);
        PageResult<SysDict> pageResult = new PageResult<SysDict>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }


}
