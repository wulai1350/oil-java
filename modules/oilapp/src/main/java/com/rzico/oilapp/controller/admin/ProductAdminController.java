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
import com.rzico.oilapp.entity.Product;
import com.rzico.oilapp.entity.Warehouse;
import com.rzico.oilapp.service.CompanyService;
import com.rzico.oilapp.service.CustomerService;
import com.rzico.oilapp.service.OrderService;
import com.rzico.oilapp.service.ProductService;
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
 * 产品档案控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "产品档案接口")
@RestController
@RequestMapping("/admin/product")
public class ProductAdminController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询产品档案
     *
     * @return
    */
    @ApiOperation("分页查询产品档案")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "所属公司", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "供应商", dataType = "String", paramType = "query"),
    })
    public CommResult<Product> list(Date startDate, Date endDate,String keyword,Long companyId,Long customerId, Pageable pageable) {
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

        if (customerId!=null){
            params.put("customerId", customerId);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Product> list = productService.selectList(params);

        for (Product product:list) {

            if (product.getCustomerId()!=null) {
                Customer customer = customerService.selectByPrimaryKey(product.getCustomerId());
                if (customer!=null) {
                    product.setCustomerName(customer.getName());
                }

            }


            if (product.getCompanyId()!=null) {
                Company company = companyService.selectByPrimaryKey(product.getCompanyId());
                if (company!=null) {
                    product.setCompanyName(company.getName());
                }

            }
        }
        PageResult<Product> pageResult = new PageResult<Product>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);

    }

    /**
     * 查询单条产品档案
     *
     * @return
    */
    @ApiOperation("查询单条产品档案")
    @GetMapping("/find/{id}")
    public CommResult<Product> find(@PathVariable String id) {
        Product result = productService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("product",result);

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                data.put("customer", customer);
            }

        }

        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                data.put("company", company);
            }

        }

        return CommResult.success(data);

    }

    /**
     * 保存产品档案
     *
     * @param product
     * @return
     */
    @ApiOperation("保存产品档案")
    @PostMapping("/save")
    public CommResult<Product> save(@RequestBody Product product) {


        if (product.getCustomerId()==null) {
            return CommResult.error("请选择供应商");
        }

        if (product.getCustomerId()==null) {
            return CommResult.error("请选择供货商");
        }

        Customer customer = customerService.selectByPrimaryKey(product.getCustomerId());

        product.setCompanyId(customer.getCompanyId());
        product.setCreateDate(new Date());

        int affectCount = productService.insert(product);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Product result = productService.findById(product.getId());

        if (result.getCustomerId()!=null) {
            customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                result.setCustomerName(customer.getName());
            }

        }


        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                result.setCompanyName(company.getName());
            }

        }
        return CommResult.success(result);

    }


    /**
     * 批量删除产品档案
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除产品档案", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除产品档案,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Product> del(@PathVariable String ids) {

        String [] idds = ids.split(",");
        for (String s:idds) {
            Map<String,Object> params = new HashMap<>();
            params.put("productId",s);
            Integer w = orderService.selectRowCount(params);
            if (w>0) {
                return CommResult.error("已存在订单，勿删除");
            }

        }

        productService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新产品档案
     *
     * @param product
     * @return
     */
    @Log(desc = "更新产品档案", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新产品档案")
    @PostMapping("/update")
    public CommResult<Product> update(@RequestBody Product product) {
        int affectCount = productService.updateByPrimaryKeySelective(product);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Product result = productService.findById(product.getId());

        if (result.getCustomerId()!=null) {
            Customer customer = customerService.selectByPrimaryKey(result.getCustomerId());
            if (customer!=null) {
                result.setCustomerName(customer.getName());
            }

        }


        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                result.setCompanyName(company.getName());
            }

        }

        return CommResult.success(result);
    }

}
