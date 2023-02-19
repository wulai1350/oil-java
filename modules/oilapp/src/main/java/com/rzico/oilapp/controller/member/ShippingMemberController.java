/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
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
import com.rzico.oilapp.entity.Order;
import com.rzico.oilapp.entity.Shipping;
import com.rzico.oilapp.service.CompanyService;
import com.rzico.oilapp.service.ShippingService;
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
 * 提货管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-02-14
 */
@Api(description = "提货管理接口")
@RestController
@RequestMapping("/member/shipping")
public class ShippingMemberController extends BaseController {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private CompanyService companyService;


    /**
     * 分页查询提货管理
     *
     * @return
    */
    @ApiOperation("分页查询提货管理")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<Shipping> list(Date startDate, Date endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }


        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Shipping> list = shippingService.selectList(params);
        PageResult<Shipping> pageResult = new PageResult<Shipping>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条提货管理
     *
     * @return
    */
    @ApiOperation("查询单条提货管理")
    @GetMapping("/find/{id}")
    public CommResult<Shipping> find(@PathVariable String id) {
        Shipping result = shippingService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("shipping",result);
        return CommResult.success(data);

    }

    /**
     * 保存提货管理
     *
     * @param shipping
     * @return
     */
    @ApiOperation("保存提货管理")
    @PostMapping("/save")
    public CommResult<Shipping> save(@RequestBody Shipping shipping) {
        Company company = companyService.getCurrent();
        shipping.setCreateDate(new Date());
        shipping.setStatus(0);
        shipping.setCompanyId(company.getId());
        int affectCount = shippingService.insertUseGeneratedKeys(shipping);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Shipping result = shippingService.findById(shipping.getId());
        return CommResult.success(result);

    }


    /**
     * 关闭提货
     *
     * @param id
     * @return
     */
    @Log(desc = "关闭提货", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("关闭提货")
    @PostMapping("/cancel")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "query")
    })
    public CommResult<Order> cancel(Long id) {
        Shipping shipping = shippingService.selectByPrimaryKey(id);
        if (shipping.getStatus()==2) {
            return CommResult.error("已提货不能关闭");
        }
        if (shipping.getStatus()==1) {
            return CommResult.error("已预约不能关闭");
        }

        shippingService.cancel(shipping);
        shipping = shippingService.findById(shipping.getId());
        return CommResult.success(shipping);
    }

}
