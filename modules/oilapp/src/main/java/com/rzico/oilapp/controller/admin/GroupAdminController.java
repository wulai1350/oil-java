/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-14
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
import com.rzico.oilapp.entity.Group;
import com.rzico.oilapp.service.GroupService;
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
 * 集团资料控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-14
 */
@Api(description = "集团资料接口")
@RestController
@RequestMapping("/admin/group")
public class GroupAdminController extends BaseController {

    @Autowired
    private GroupService groupService;

    /**
     * 分页查询集团资料
     *
     * @return
    */
    @ApiOperation("分页查询集团资料")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<Group> list(Date startDate, Date endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }


        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Group> list = groupService.selectList(params);
        PageResult<Group> pageResult = new PageResult<Group>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条集团资料
     *
     * @return
    */
    @ApiOperation("查询单条集团资料")
    @GetMapping("/find/{id}")
    public CommResult<Group> find(@PathVariable String id) {
        Group result = groupService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("group",result);
        return CommResult.success(data);

    }

    /**
     * 保存集团资料
     *
     * @param group
     * @return
     */
    @ApiOperation("保存集团资料")
    @PostMapping("/save")
    public CommResult<Group> save(@RequestBody Group group) {
        int affectCount = groupService.insert(group);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Group result = groupService.findById(group.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除集团资料
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除集团资料", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除集团资料,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Group> del(@PathVariable String ids) {

        groupService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新集团资料
     *
     * @param group
     * @return
     */
    @Log(desc = "更新集团资料", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新集团资料")
    @PostMapping("/update")
    public CommResult<Group> update(@RequestBody Group group) {
        int affectCount = groupService.updateByPrimaryKeySelective(group);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Group result = groupService.findById(group.getId());
        return CommResult.success(result);
    }

}
