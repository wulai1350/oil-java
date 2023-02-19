/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-14
*/
package com.rzico.oilapp.controller.member;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.Group;
import com.rzico.oilapp.service.GroupService;
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
 * 集团资料控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-14
 */
@Api(description = "集团资料接口")
@RestController
@RequestMapping("/member/group")
public class GroupMemberController extends BaseController {

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
}
