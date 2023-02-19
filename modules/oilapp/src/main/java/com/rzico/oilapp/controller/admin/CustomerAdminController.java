/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
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
import com.rzico.oilapp.entity.Group;
import com.rzico.oilapp.service.*;
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
 * 客户资料控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "客户资料接口")
@RestController
@RequestMapping("/admin/customer")
public class CustomerAdminController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 分页查询客户资料
     *
     * @return
    */
    @ApiOperation("分页查询客户资料")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "客户类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "客户类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query")
    })
    public CommResult<Customer> list(Date startDate, Date endDate,Integer type,Integer status,String keyword, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (type!=null){
            params.put("type", type);
        }

        if (status!=null){
            params.put("status", status);
        }

        if (keyword!=null){
            params.put("keyword", keyword);
        }
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Customer> list = customerService.selectList(params);

        for (Customer customer:list) {
            if (customer.getGroupId()!=null) {
                Group group = groupService.selectByPrimaryKey(customer.getGroupId());
                if (group!=null) {
                    customer.setGroupName(group.getName());
                }
            }
        }

        PageResult<Customer> pageResult = new PageResult<Customer>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条客户资料
     *
     * @return
    */
    @ApiOperation("查询单条客户资料")
    @GetMapping("/find/{id}")
    public CommResult<Customer> find(@PathVariable String id) {
        Customer result = customerService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("customer",result);

        if (result.getGroupId()!=null) {
            data.put("group",groupService.selectByPrimaryKey(result.getGroupId()));
        }

        if (result.getCompanyId()!=null) {
            data.put("company",companyService.selectByPrimaryKey(result.getCompanyId()));
        }

        return CommResult.success(data);

    }

    /**
     * 批量删除客户资料
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除客户资料", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除客户资料,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Customer> del(@PathVariable String ids) {

        String [] idds = ids.split(",");
        for (String s:idds) {
            Map<String,Object> params = new HashMap<>();
            params.put("supplierId",s);
            Integer w = orderService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在订单，勿删除");
            }
            params.clear();
            params.put("customerId",s);
            w = orderService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在订单，勿删除");
            }

            w = productService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在商品，勿删除");
            }

            w = warehouseService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在油库，勿删除");
            }

        }

        customerService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 保存客户资料
     *
     * @param customer
     * @return
     */
    @ApiOperation("保存客户资料")
    @PostMapping("/save")
    public CommResult<Customer> save(@RequestBody Customer customer) {

        Company company = companyService.getCurrent();

        if (company==null) {
            return CommResult.error("没有登录");
        }

        customer.setCompanyId(company.getId());
        customer.setCreateDate(new Date());

        if (customerService.checkExists(customer)) {
            return CommResult.error("客户已经存在");
        }
        int affectCount = customerService.insert(customer);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Customer result = customerService.findById(customer.getId());

        if (result.getGroupId()!=null) {
            Group group = groupService.selectByPrimaryKey(result.getGroupId());
            if (group!=null) {
                result.setGroupName(group.getName());
            }
        }
        return CommResult.success(result);

    }

    /**
     * 更新客户资料
     *
     * @param customer
     * @return
     */
    @Log(desc = "更新客户资料", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新客户资料")
    @PostMapping("/update")
    public CommResult<Customer> update(@RequestBody Customer customer) {

        Customer source = customerService.selectByPrimaryKey(customer.getId());
        customer.setCompanyId(source.getCompanyId());

        if (customerService.checkExists(customer)) {
            return CommResult.error("客户已经存在");
        }

        int affectCount = customerService.updateByPrimaryKeySelective(customer);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Customer result = customerService.findById(customer.getId());

        if (result.getGroupId()!=null) {
            Group group = groupService.selectByPrimaryKey(result.getGroupId());
            if (group!=null) {
                result.setGroupName(group.getName());
            }
        }
        return CommResult.success(result);
    }

}
