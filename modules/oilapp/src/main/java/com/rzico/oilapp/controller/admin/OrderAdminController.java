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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * 交易订单控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "交易订单接口")
@RestController
@RequestMapping("/admin/order")
public class OrderAdminController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductService productService;

    /**
     * 分页查询交易订单
     *
     * @return
    */
    @ApiOperation("分页查询交易订单")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "所属公司", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "supplierId", value = "供应商", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "warehouseId", value = "油库", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customerId", value = "采购商", dataType = "String", paramType = "query")
    })
    public CommResult<Order> list(Date startDate, Date endDate,String keyword,Integer status,Long companyId,Long warehouseId,Long supplierId,Long customerId, Pageable pageable) {

        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (companyId!=null){
            params.put("companyId", companyId);
        }

        if (supplierId!=null){
            params.put("supplierId", supplierId);
        }

        if (customerId!=null){
            params.put("customerId", customerId);
        }

        if (keyword!=null){
            params.put("keyword", keyword);
        }

        if (warehouseId!=null){
            params.put("warehouseId", warehouseId);
        }

        if (status!=null){
            params.put("status", status);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Order> list = orderService.selectList(params);
        PageResult<Order> pageResult = new PageResult<Order>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条交易订单
     *
     * @return
    */
    @ApiOperation("查询单条交易订单")
    @GetMapping("/find/{id}")
    public CommResult<Order> find(@PathVariable String id) {
        Order result = orderService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("order",result);

        if (result.getBuyyer().getGroupId()!=null) {
            Group group = groupService.selectByPrimaryKey(result.getBuyyer().getGroupId());
            if (group!=null) {
                result.getBuyyer().setGroupName(group.getName());
            }
        }

        if (result.getSeller().getGroupId()!=null) {
            Group group = groupService.selectByPrimaryKey(result.getSeller().getGroupId());
            if (group!=null) {
                result.getSeller().setGroupName(group.getName());
            }
        }


        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                data.put("company",company);
            }
        }

        return CommResult.success(data);

    }


    /**
     * 保存交易订单
     *
     * @param order
     * @return
     */
    @ApiOperation("保存交易订单")
    @PostMapping("/save")
    public CommResult<Order> save(@RequestBody Order order) {

        Company company = companyService.getCurrent();

        if (company==null) {
            return CommResult.error("没有登录");
        }

        if (company.getStatus().equals(0)) {
            return CommResult.error("账号末认证");
        }

        Customer supplier = customerService.selectByPrimaryKey(order.getSupplierId());

        if (supplier.getStatus().equals(0)) {
            return CommResult.error("供货方末认证");
        }

        Customer customer = customerService.selectByPrimaryKey(order.getCustomerId());

        if (customer.getStatus().equals(0)) {
            return CommResult.error("购买方末认证");
        }

        order.setCompanyId(company.getId());
        order.setCreateDate(new Date());

        order.setAmountPaid(BigDecimal.ZERO);
        order.setAmountPayable(order.getPrice().multiply(order.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_DOWN));

        Product product = productService.selectByPrimaryKey(order.getProductId());
        if (product==null) {
            return CommResult.error("无效商品资料");
        }
        order.setLimitDay(product.getLimitDay());
        order.setOrderMethod(product.getOrderMethod());

        int affectCount = orderService.insert(order);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);

    }

    /**
     * 批量删除交易订单
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除交易订单", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除交易订单,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Order> del(@PathVariable String ids) {


        String [] idds = ids.split(",");
        for (String s:idds) {
            Order order = orderService.selectByPrimaryKey(s);
            if (!order.getStatus().equals(10)) {
                return CommResult.error("关闭的订单才能删除");
            }
        }

        orderService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新交易订单
     *
     * @param order
     * @return
     */
    @Log(desc = "更新交易订单", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新交易订单")
    @PostMapping("/update")
    public CommResult<Order> update(@RequestBody Order order) {
        int affectCount = orderService.updateByPrimaryKeySelective(order);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);
    }


    /**
     * 接单
     *
     * @param id
     * @return
     */
    @Log(desc = "接单", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("接单")
    @PostMapping("/confirm")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
    })
    public CommResult<Order> confirm(Long id) {
        Order order = orderService.selectByPrimaryKey(id);
        if (order.getStatus()!=0) {
            return CommResult.error("此订单状态不能接单");
        }
        if (order.getStatus()==10) {
            return CommResult.error("已取消不能下订");
        }

        orderService.confirm(order);
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);
    }


    /**
     * 下定
     *
     * @param id
     * @return
     */
    @Log(desc = "下定", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("下定")
    @PostMapping("/partialPayment")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
    })
    public CommResult<Order> partialPayment(Long id) {
        Order order = orderService.selectByPrimaryKey(id);
        if (order.getStatus()==4) {
            return CommResult.error("已下订不能重复");
        }
        if (order.getStatus()==5) {
            return CommResult.error("已付款不能下订");
        }
        if (order.getStatus()==10) {
            return CommResult.error("已取消不能下订");
        }
        if (order.getStatus()==9) {
            return CommResult.error("已完成不能下订");
        }
        orderService.partialPayment(order);
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);
    }


    /**
     * 付款
     *
     * @param id
     * @return
     */
    @Log(desc = "付款", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("付款")
    @PostMapping("/payment")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
    })
    public CommResult<Order> payment(Long id) {
        Order order = orderService.selectByPrimaryKey(id);
        if (order.getStatus()==5) {
            return CommResult.error("已付款不能下订");
        }
        if (order.getStatus()==10) {
            return CommResult.error("已取消不能下订");
        }
        if (order.getStatus()==9) {
            return CommResult.error("已完成不能下订");
        }
        orderService.payment(order);
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);
    }

    /**
     * 订单完成
     *
     * @param id
     * @return
     */
//    @Log(desc = "订单完成", type = Log.LOG_TYPE.UPDATE)
//    @ApiOperation("订单完成")
//    @PostMapping("/complete")
//    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
//    })
//    public CommResult<Order> complete(Long id) {
//        Order order = orderService.selectByPrimaryKey(id);
//        order.setStatus(9);
//        orderService.updateByPrimaryKey(order);
//        Order result = orderService.findById(order.getId());
//        return CommResult.success(result);
//    }


    /**
     * 关闭订单
     *
     * @param id
     * @return
     */
    @Log(desc = "关闭订单", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("关闭订单")
    @PostMapping("/cancel")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
    })
    public CommResult<Order> cancel(Long id) {
        Order order = orderService.selectByPrimaryKey(id);
        if (order.getStatus()==4) {
            return CommResult.error("已下订不能关闭");
        }
        if (order.getStatus()==5) {
            return CommResult.error("已付款不能关闭");
        }
        if (order.getStatus()==10) {
            return CommResult.error("已取消不能关闭");
        }
        if (order.getStatus()==9) {
            return CommResult.error("已完成不能关闭");
        }
        orderService.cancel(order);
        Order result = orderService.findById(order.getId());
        return CommResult.success(result);
    }


}
