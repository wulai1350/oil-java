/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.controller.member;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysArea;
import com.rzico.core.service.SysAreaService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.entity.Customer;
import com.rzico.oilapp.entity.Warehouse;
import com.rzico.oilapp.service.CompanyService;
import com.rzico.oilapp.service.CustomerService;
import com.rzico.oilapp.service.OrderService;
import com.rzico.oilapp.service.WarehouseService;
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
 * 油库管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "油库管理接口")
@RestController
@RequestMapping("/member/warehouse")
public class WarehouseMemberController extends BaseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SysAreaService sysAreaService;



    @Autowired
    private OrderService orderService;
    /**
     * 分页查询油库管理
     *
     * @return
    */
    @ApiOperation("分页查询油库管理")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "供应商", dataType = "String", paramType = "query")
    })
    public CommResult<Warehouse> list(Date startDate, Date endDate,String keyword,Long customerId, Pageable pageable) {
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

        Company company = companyService.getCurrent();

        if (company==null) {
            return CommResult.error("没有登录");
        }

        params.put("companyId", company.getId());

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Warehouse> list = warehouseService.selectList(params);
        PageResult<Warehouse> pageResult = new PageResult<Warehouse>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条油库管理
     *
     * @return
    */
    @ApiOperation("查询单条油库管理")
    @GetMapping("/find/{id}")
    public CommResult<Warehouse> find(@PathVariable String id) {
        Warehouse result = warehouseService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("warehouse",result);

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            data.put("customer",customer);

        }

        if (result.getAreaId()!=null) {
            SysArea sysArea = sysAreaService.selectByPrimaryKey(result.getAreaId());
            data.put("area",sysArea);

        }

        return CommResult.success(data);

    }

    /**
     * 保存油库管理
     *
     * @param warehouse
     * @return
     */
    @ApiOperation("保存油库管理")
    @PostMapping("/save")
    public CommResult<Warehouse> save(@RequestBody Warehouse warehouse) {

        Company company = companyService.getCurrent();

        if (company==null) {
            return CommResult.error("没有登录");
        }

        if (warehouse.getCustomerId()==null) {
            return CommResult.error("请选择供货商");
        }

        warehouse.setCompanyId(company.getId());
        warehouse.setCreateDate(new Date());

        int affectCount = warehouseService.insert(warehouse);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Warehouse result = warehouseService.findById(warehouse.getId());
        return CommResult.success(result);

    }


    /**
     * 批量删除油库管理
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除油库管理", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除油库管理,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Warehouse> del(@PathVariable String ids) {

        String [] idds = ids.split(",");
        for (String s:idds) {
            Map<String,Object> params = new HashMap<>();
            params.put("warehouseId",s);
            Integer w = orderService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在订单，勿删除");
            }

        }

        warehouseService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新油库管理
     *
     * @param warehouse
     * @return
     */
    @Log(desc = "更新油库管理", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新油库管理")
    @PostMapping("/update")
    public CommResult<Warehouse> update(@RequestBody Warehouse warehouse) {
        int affectCount = warehouseService.updateByPrimaryKeySelective(warehouse);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Warehouse result = warehouseService.findById(warehouse.getId());
        return CommResult.success(result);
    }

}
