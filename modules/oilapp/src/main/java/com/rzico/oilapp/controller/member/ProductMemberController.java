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
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.entity.Customer;
import com.rzico.oilapp.entity.Product;
import com.rzico.oilapp.service.CompanyService;
import com.rzico.oilapp.service.CustomerService;
import com.rzico.oilapp.service.OrderService;
import com.rzico.oilapp.service.ProductService;
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
 * 产品档案控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "产品档案接口")
@RestController
@RequestMapping("/member/product")
public class ProductMemberController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;


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
            @ApiImplicitParam(name = "goodsCategoryId", value = "商品库分类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "供应商", dataType = "String", paramType = "query")
    })
    public CommResult<Product> list(Date startDate, Date endDate,Long goodsCategoryId, String keyword,Long customerId, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (goodsCategoryId!=null){
            params.put("goodsCategoryId", goodsCategoryId);
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
        List<Product> list = productService.selectList(params);
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
            data.put("customer",customer);

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


        Company company = companyService.getCurrent();

        if (company==null) {
            return CommResult.error("没有登录");
        }

        if (product.getCustomerId()==null) {
            return CommResult.error("请选择供货商");
        }

        product.setCompanyId(company.getId());
        product.setCreateDate(new Date());

        int affectCount = productService.insert(product);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Product result = productService.findById(product.getId());
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
        return CommResult.success(result);
    }

}
