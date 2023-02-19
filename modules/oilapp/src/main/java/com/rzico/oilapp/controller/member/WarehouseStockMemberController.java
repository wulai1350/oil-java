/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
*/
package com.rzico.oilapp.controller.member;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.*;
import com.rzico.oilapp.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-02-14
 */
@Api(description = "库存管理接口")
@RestController
@RequestMapping("/member/warehouseStock")

public class WarehouseStockMemberController extends BaseController {

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
            @ApiImplicitParam(name = "warehouseId", value = "油库", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "商品", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "买家", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "买家/油库", dataType = "String", paramType = "query"),
    })
    public CommResult<WarehouseStock> list(Date startDate, Date endDate, Long companyId,String keyword, Long warehouseId, Long productId, Long customerId, Pageable pageable) {

        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        Company company = companyService.getCurrent();
        params.put("companyId", company.getId());


        if (keyword!=null){
            params.put("keyword", keyword);
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


            warehouseStock.setCompanyName(company.getName());


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
