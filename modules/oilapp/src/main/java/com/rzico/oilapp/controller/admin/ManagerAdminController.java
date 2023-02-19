/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
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
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.entity.Customer;
import com.rzico.oilapp.entity.Manager;
import com.rzico.oilapp.entity.Product;
import com.rzico.oilapp.service.CustomerService;
import com.rzico.oilapp.service.ManagerService;
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
 * 客户经理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-02-14
 */
@Api(description = "客户经理接口")
@RestController
@RequestMapping("/admin/manager")
public class ManagerAdminController extends BaseController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CustomerService customerService;


    /**
     * 分页查询客户经理
     *
     * @return
    */
    @ApiOperation("分页查询客户经理")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "供应商", dataType = "String", paramType = "query"),
    })
    public CommResult<Manager> list(Date startDate, Date endDate,String keyword,Long customerId, Pageable pageable) {

        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (keyword!=null){
            params.put("keyword", keyword);
        }

        if (customerId!=null){
            params.put("customerId", customerId);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Manager> list = managerService.selectList(params);

        for (Manager manager:list) {

            if (manager.getCustomerId()!=null) {
                Customer customer = customerService.selectByPrimaryKey(manager.getCustomerId());
                if (customer!=null) {
                    manager.setCustomerName(customer.getName());
                }

            }

        }

        PageResult<Manager> pageResult = new PageResult<Manager>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条客户经理
     *
     * @return
    */
    @ApiOperation("查询单条客户经理")
    @GetMapping("/find/{id}")
    public CommResult<Manager> find(@PathVariable String id) {
        Manager result = managerService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("manager",result);

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                data.put("customer", customer);
            }

        }

        return CommResult.success(data);

    }

    /**
     * 保存客户经理
     *
     * @param manager
     * @return
     */
    @ApiOperation("保存客户经理")
    @PostMapping("/save")
    public CommResult<Manager> save(@RequestBody Manager manager) {

        int affectCount = managerService.insertUseGeneratedKeys(manager);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Manager result = managerService.findById(manager.getId());

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                result.setCustomerName(customer.getName());
            }

        }

        return CommResult.success(result);

    }


    /**
     * 批量删除客户经理
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除客户经理", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除客户经理,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Manager> del(@PathVariable String ids) {

        managerService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新客户经理
     *
     * @param manager
     * @return
     */
    @Log(desc = "更新客户经理", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新客户经理")
    @PostMapping("/update")
    public CommResult<Manager> update(@RequestBody Manager manager) {
        int affectCount = managerService.updateByPrimaryKeySelective(manager);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Manager result = managerService.findById(manager.getId());

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                result.setCustomerName(customer.getName());
            }

        }

        return CommResult.success(result);
    }

}
