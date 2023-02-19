/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-16
*/
package com.rzico.oilapp.controller.member;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.Goods;
import com.rzico.oilapp.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品库分类控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-16
 */
@Api(description = "商品库分类接口")
@RestController
@RequestMapping("/member/goods")
public class GoodsMemberController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询商品库分类
     *
     * @return
    */
    @ApiOperation("分页查询商品库分类")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsCategoryId", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query")
    })
    public CommResult<Goods> list(Long goodsCategoryId,String keyword, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);

        if (goodsCategoryId!=null){
           params.put("goodsCategoryId", goodsCategoryId);
        }

        if (keyword!=null){
            params.put("keyword", keyword);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Goods> list = goodsService.selectList(params);
        PageResult<Goods> pageResult = new PageResult<Goods>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

}
