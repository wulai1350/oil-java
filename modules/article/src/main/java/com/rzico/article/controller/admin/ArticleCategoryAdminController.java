/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.article.entity.ArticleCategory;
import com.rzico.article.service.ArticleCategoryService;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/admin/articleCategory")
public class ArticleCategoryAdminController extends BaseController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    /**
     * 分页查询文章分类
     *
     * @return
    */
    @ApiOperation("分页查询文章分类")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<ArticleCategory> list(String startDate, String endDate, Pageable pageable) {
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

    /**
     * 保存文章分类
     *
     * @param articleCategory
     * @return
     */
    @ApiOperation("保存文章分类")
    @PostMapping("/save")
    public CommResult<ArticleCategory> save(@RequestBody ArticleCategory articleCategory) {
        Assert.notNull(articleCategory,"获取数据失败");
        Assert.notNull(articleCategory.getName(), "菜单名称为空");

        articleCategory.setCreateDate(new Date());
        int affectCount = articleCategoryService.insertUseGeneratedKeys(articleCategory);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        ArticleCategory result = articleCategoryService.findById(articleCategory.getId());
        return CommResult.success(result);

    }

    /**
     * 批量删除文章分类
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除文章分类", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除文章分类,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<ArticleCategory> del(@PathVariable String ids) {

        articleCategoryService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新文章分类
     *
     * @param articleCategory
     * @return
     */
    @Log(desc = "更新文章分类", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新文章分类")
    @PostMapping("/update")
    public CommResult<ArticleCategory> update(@RequestBody ArticleCategory articleCategory) {
        int affectCount = articleCategoryService.updateByPrimaryKeySelective(articleCategory);
        if (affectCount <= 0){
            return CommResult.error();
        }
        ArticleCategory result = articleCategoryService.findById(articleCategory.getId());
        return CommResult.success(result);
    }

}
