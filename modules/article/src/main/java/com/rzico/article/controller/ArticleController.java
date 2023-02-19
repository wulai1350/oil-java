/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.controller;

import com.alipay.api.domain.Hit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.article.entity.Article;
import com.rzico.article.service.ArticleService;
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
 * 文章管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-03-23
 */
@Api(description = "文章管理接口")
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询文章管理
     *
     * @return
     */
    @ApiOperation("分页查询文章管理")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "articleCategoryId", value = "分类 id", dataType = "Long", paramType = "query"),
    })
    public CommResult<Article> list(String title,String mchId,Integer articleType,Integer articleCategoryId, Pageable pageable) {

        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);

        params.put("deleted", false);
        params.put("isDraft", false);

        if (title!=null) {
            params.put("title", title);
        }

        if (articleType==null) {
            articleType = 0;
        }
        params.put("articleType", articleType);

        if (articleCategoryId!=null) {
           params.put("treePath", String.valueOf(articleCategoryId));
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
     * 阅读计数器
     *
     * @return
     */
    @ApiOperation("阅读计数器")
    @PostMapping("/hits")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "公司id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "openId", value = "用户 id", dataType = "String", paramType = "query"),
    })
    public CommResult<Article> find(Long id,Long companyId,String openId) {

        Article result = articleService.findById(id);

        articleService.hits(result,companyId,openId);
        return CommResult.success();

    }

}
