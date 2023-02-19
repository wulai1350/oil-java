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
import com.rzico.oilapp.entity.Car;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.entity.Customer;
import com.rzico.oilapp.entity.Product;
import com.rzico.oilapp.service.CarService;
import com.rzico.oilapp.service.CompanyService;
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
 * 车辆管理控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-02-14
 */
@Api(description = "车辆管理接口")
@RestController
@RequestMapping("/admin/car")
public class CarAdminController extends BaseController {

    @Autowired
    private CarService carService;

    @Autowired
    private CompanyService companyService;

    /**
     * 分页查询车辆管理
     *
     * @return
    */
    @ApiOperation("分页查询车辆管理")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "所属公司", dataType = "String", paramType = "query"),
    })
    public CommResult<Car> list(Date startDate, Date endDate,Long companyId, Pageable pageable) {
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

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Car> list = carService.selectList(params);


        for (Car car:list) {

            if (car.getCompanyId()!=null) {
                Company company = companyService.selectByPrimaryKey(car.getCompanyId());
                if (company!=null) {
                    car.setCompanyName(company.getName());
                }

            }
        }

        PageResult<Car> pageResult = new PageResult<Car>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条车辆管理
     *
     * @return
    */
    @ApiOperation("查询单条车辆管理")
    @GetMapping("/find/{id}")
    public CommResult<Car> find(@PathVariable String id) {
        Car result = carService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("car",result);


        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                result.setCompanyName(company.getName());
            }

        }

        return CommResult.success(data);

    }

    /**
     * 保存车辆管理
     *
     * @param car
     * @return
     */
    @ApiOperation("保存车辆管理")
    @PostMapping("/save")
    public CommResult<Car> save(@RequestBody Car car) {

        car.setCreateDate(new Date());

        int affectCount = carService.insertUseGeneratedKeys(car);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Car result = carService.findById(car.getId());

        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                result.setCompanyName(company.getName());
            }

        }

        return CommResult.success(result);

    }


    /**
     * 批量删除车辆管理
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除车辆管理", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除车辆管理,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Car> del(@PathVariable String ids) {

        carService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新车辆管理
     *
     * @param car
     * @return
     */
    @Log(desc = "更新车辆管理", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新车辆管理")
    @PostMapping("/update")
    public CommResult<Car> update(@RequestBody Car car) {
        int affectCount = carService.updateByPrimaryKeySelective(car);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Car result = carService.findById(car.getId());

        if (result.getCompanyId()!=null) {
            Company company = companyService.selectByPrimaryKey(result.getCompanyId());
            if (company!=null) {
                result.setCompanyName(company.getName());
            }

        }

        return CommResult.success(result);
    }

}
