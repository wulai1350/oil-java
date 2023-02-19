/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.article.entity.ArticleCategory;
import com.rzico.article.service.ArticleCategoryService;
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

/**
 * 文章分类控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-03-23
 */
@Api(description = "文章分类接口")
@RestController
@RequestMapping("/articleCategory")
public class ArticleCategoryController extends BaseController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    /**
     * 分页查询文章分类
     *
     * @return
     */
    @ApiOperation("分页查询文章分类")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleCategoryId", value = "分类Id", dataType = "String", paramType = "query")
    })
    public CommResult<ArticleCategory> list(String mchId, Long articleCategoryId, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sortField", "tree_path");
        params.put("sortType", "asc");

        params.put("sortField", "tree_path");
        params.put("sortType", "ASC");
        List<ArticleCategory> list = articleCategoryService.selectTree(params);
        return CommResult.success(list);
    }

    /**
     * 查询单条文章分类
     *
     * @return
     */
    @ApiOperation("查询单条文章分类")
    @GetMapping("/find/{id}")
    public CommResult<ArticleCategory> find(@PathVariable String id) {
        ArticleCategory result = articleCategoryService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("articleCategory",result);
        return CommResult.success(data);

    }

}
