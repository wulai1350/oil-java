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
import com.rzico.oilapp.entity.*;
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
 * 库存管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-02-14
 */
@Api(description = "库存管理接口")
@RestController
@RequestMapping("/admin/warehouseStock")

public class WarehouseStockAdminController extends BaseController {

    @Autowired
    private WarehouseStockService warehouseStockService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductService productService;

    /**
     * 分页查询库存管理
     *
     * @return
    */
    @ApiOperation("分页查询库存管理")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "所属公司", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "warehouseId", value = "油库", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "商品", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "买家", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "买家/油库", dataType = "String", paramType = "query"),
    })
    public CommResult<WarehouseStock> list(Date startDate, Date endDate,String keyword, Long companyId, Long warehouseId, Long productId, Long customerId, Pageable pageable) {
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

        if (companyId!=null){
            params.put("companyId", companyId);
        }

        if (warehouseId!=null){
            params.put("warehouseId", warehouseId);
        }


        if (productId!=null){
            params.put("productId", productId);
        }

        if (customerId!=null){
            params.put("customerId", customerId);
        }


        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<WarehouseStock> list = warehouseStockService.selectList(params);


        for (WarehouseStock warehouseStock:list) {

            if (warehouseStock.getCustomerId()!=null) {
                Customer customer = customerService.selectByPrimaryKey(warehouseStock.getCustomerId());
                if (customer!=null) {
                    warehouseStock.setCustomerName(customer.getName());
                }

            }


            if (warehouseStock.getCompanyId()!=null) {
                Company company = companyService.selectByPrimaryKey(warehouseStock.getCompanyId());
                if (company!=null) {
                    warehouseStock.setCompanyName(company.getName());
                }

            }

            if (warehouseStock.getWarehouseId()!=null) {
                Warehouse warehouse = warehouseService.selectByPrimaryKey(warehouseStock.getWarehouseId());
                if (warehouse!=null) {
                    warehouseStock.setWarehouseName(warehouse.getName());
                }

            }

            if (warehouseStock.getProductId()!=null) {
                Product product = productService.selectByPrimaryKey(warehouseStock.getProductId());
                if (product!=null) {
                    warehouseStock.setProductName(product.getName());
                }

            }

        }

        PageResult<WarehouseStock> pageResult = new PageResult<WarehouseStock>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }


}
