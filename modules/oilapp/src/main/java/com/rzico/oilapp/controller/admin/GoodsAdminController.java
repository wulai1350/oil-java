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
import com.rzico.oilapp.entity.Goods;
import com.rzico.oilapp.service.GoodsService;
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
@RequestMapping("/admin/goods")
public class GoodsAdminController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询商品库分类
     *
     * @return
    */
    @ApiOperation("分页查询商品库分类")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryId", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<Goods> list(Date startDate, Date endDate,Long goodsCategoryId,  Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (goodsCategoryId!=null){
            params.put("goodsCategoryId", goodsCategoryId);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Goods> list = goodsService.selectList(params);
        PageResult<Goods> pageResult = new PageResult<Goods>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条商品库分类
     *
     * @return
    */
    @ApiOperation("查询单条商品库分类")
    @GetMapping("/find/{id}")
    public CommResult<Goods> find(@PathVariable String id) {
        Goods result = goodsService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("goods",result);
        return CommResult.success(data);

    }

    /**
     * 保存商品库分类
     *
     * @param goods
     * @return
     */
    @ApiOperation("保存商品库分类")
    @PostMapping("/save")
    public CommResult<Goods> save(@RequestBody Goods goods) {
        int affectCount = goodsService.insert(goods);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Goods result = goodsService.findById(goods.getId());
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
    public CommResult<Goods> del(@PathVariable String ids) {

        goodsService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新商品库分类
     *
     * @param goods
     * @return
     */
    @Log(desc = "更新商品库分类", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新商品库分类")
    @PostMapping("/update")
    public CommResult<Goods> update(@RequestBody Goods goods) {
        int affectCount = goodsService.updateByPrimaryKeySelective(goods);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Goods result = goodsService.findById(goods.getId());
        return CommResult.success(result);
    }

}
