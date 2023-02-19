/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-16
*/
package com.rzico.oilapp.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.oilapp.entity.GoodsCategroy;
import com.rzico.oilapp.service.GoodsCategroyService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * 商品库分类控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-16
 */
@Api(description = "商品库分类接口")
@RestController
@RequestMapping("/admin/goodsCategroy")
public class GoodsCategroyAdminController extends BaseController {

    @Autowired
    private GoodsCategroyService goodsCategroyService;

    /**
     * 分页查询商品库分类
     *
     * @return
    */
    @ApiOperation("分页查询商品库分类")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<GoodsCategroy> list(Date startDate, Date endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }


        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<GoodsCategroy> list = goodsCategroyService.selectList(params);
        PageResult<GoodsCategroy> pageResult = new PageResult<GoodsCategroy>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条商品库分类
     *
     * @return
    */
    @ApiOperation("查询单条商品库分类")
    @GetMapping("/find/{id}")
    public CommResult<GoodsCategroy> find(@PathVariable String id) {
        GoodsCategroy result = goodsCategroyService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("goodsCategroy",result);
        return CommResult.success(data);

    }

    /**
     * 保存商品库分类
     *
     * @param goodsCategroy
     * @return
     */
    @ApiOperation("保存商品库分类")
    @PostMapping("/save")
    public CommResult<GoodsCategroy> save(@RequestBody GoodsCategroy goodsCategroy) {
        int affectCount = goodsCategroyService.insert(goodsCategroy);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        GoodsCategroy result = goodsCategroyService.findById(goodsCategroy.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除商品库分类
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除商品库分类", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除商品库分类,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<GoodsCategroy> del(@PathVariable String ids) {

        goodsCategroyService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新商品库分类
     *
     * @param goodsCategroy
     * @return
     */
    @Log(desc = "更新商品库分类", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新商品库分类")
    @PostMapping("/update")
    public CommResult<GoodsCategroy> update(@RequestBody GoodsCategroy goodsCategroy) {
        int affectCount = goodsCategroyService.updateByPrimaryKeySelective(goodsCategroy);
        if (affectCount <= 0){
            return CommResult.error();
        }
        GoodsCategroy result = goodsCategroyService.findById(goodsCategroy.getId());
        return CommResult.success(result);
    }

}
