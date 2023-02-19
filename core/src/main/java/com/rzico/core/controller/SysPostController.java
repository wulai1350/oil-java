/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.core.entity.SysPost;
import com.rzico.core.service.SysPostService;
import com.rzico.rabbit.QueuePropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-07
 */
@Api(description = "职位表接口")
@RestController
@RequestMapping("/admin/sysPost")
public class SysPostController extends BaseController {

    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private QueuePropertiesConfig queuePropertiesConfig;



    @Autowired
    private SysMchService sysMchService;

    /**
     * 查询分页记录
     *
     * @return
    */
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<SysPost> list(String startDate, String endDate,
                                    String name, Integer status, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sortField", pageable.getSortField()==null?"create_date":pageable.getSortField());
        params.put("sortType", pageable.getDirection()==null?"DESC":pageable.getDirection().name());
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }
        if (StringUtils.isNotEmpty(name)){
            params.put("name", name);
        }
        if (null != status){
            params.put("status", status);
        }

//        SysMch sysMch = sysMchService.getCurrent();
//        params.put("mchId", sysMch.getId());

        params.put("delFlag", false);
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysPost> list = sysPostService.selectList(params);
        PageResult<SysPost> pageResult = new PageResult<SysPost>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysPost> find(@PathVariable String id) {
        SysPost result = sysPostService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysPost",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysPost
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")
    public CommResult<SysPost> save(@RequestBody SysPost sysPost) {
        Assert.notNull(sysPost.getName(), "名称不能为空");


        SysMch sysMch = sysMchService.getCurrent();
        sysPost.setMchId(sysMch.getId());

        //默认先设置成根节点
        sysPost.setTreePath(",");
        sysPost.setDelFlag(false);
        int affectCount = sysPostService.insert(sysPost);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysPost result = sysPostService.findById(sysPost.getId());
        return CommResult.success(result);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除记录,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysPost> del(@PathVariable String ids) {
        sysPostService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysPost
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysPost> update(@RequestBody SysPost sysPost) {
        Assert.notNull(sysPost.getId(), "ID不能为空");
        int affectCount = sysPostService.updateByPrimaryKeySelective(sysPost);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysPost result = sysPostService.findById(sysPost.getId());
        return CommResult.success(result);
    }

}
