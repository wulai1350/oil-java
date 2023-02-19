/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.article.entity.Article;
import com.rzico.article.service.ArticleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-03-23
 */
@Api(description = "文章管理接口")
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询文章管理
     *
     * @return
    */
    @ApiOperation("分页查询文章管理")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "标题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "删除标志", dataType = "Boolean", paramType = "query"),
            @ApiImplicitParam(name = "articleCategoryId", value = "分类 id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "articleType", value = "文章类型", dataType = "Long", paramType = "query")
    })
    public CommResult<Article> list(String startDate, String endDate,Long articleCategoryId,Integer articleType,String keyword,Boolean deleted, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }
        if (articleType!=null) {
            params.put("articleType",articleType);
        } else {
            params.put("articleType",0);
        }

        if (articleCategoryId!=null) {
            if (articleCategoryId==0L) {
                params.put("articleCategoryId", articleCategoryId);
            } else {
                params.put("treePath", String.valueOf(articleCategoryId));
            }
        }

        if (deleted==null) {
            deleted = false;
        }

        params.put("deleted", deleted);

        if (keyword!=null) {
            params.put("keyword", keyword);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Article> list = articleService.selectList(params);
        PageResult<Article> pageResult = new PageResult<Article>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条文章管理
     *
     * @return
    */
    @ApiOperation("查询单条文章管理")
    @GetMapping("/find/{id}")
    public CommResult<Article> find(@PathVariable String id) {
        Article result = articleService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("article",result);

        return CommResult.success(data);

    }

    /**
     * 保存文章管理
     *
     * @param article
     * @return
     */
    @ApiOperation("保存文章管理")
    @PostMapping("/save")
    public CommResult<Article> save(@RequestBody Article article) {
        article.setHits(0L);
        article.setCreateDate(new Date());
        article.setDeleted(false);
        if (article.getAuthority()==null) {
            article.setAuthority(0);
        }
        if (article.getReadTime()==null) {
            article.setReadTime(5);
        }
        Article result = articleService.save(article);
        result = articleService.findById(result.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除文章管理
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除文章管理", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除文章管理,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Article> del(@PathVariable String ids) {

        articleService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新文章管理
     *
     * @param article
     * @return
     */
    @Log(desc = "更新文章管理", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新文章管理")
    @PostMapping("/update")
    public CommResult<Article> update(@RequestBody Article article) {
        Article result = articleService.update(article);
        result = articleService.findById(result.getId());
        return CommResult.success(result);
    }

}
