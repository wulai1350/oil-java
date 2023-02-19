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
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.service.CompanyService;
import com.rzico.util.CodeGenerator;
import com.rzico.util.MD5Utils;
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
 * 公司资料控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "公司资料接口")
@RestController
@RequestMapping("/admin/company")
public class CompanyAdminController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询公司资料
     *
     * @return
    */
    @ApiOperation("分页查询公司资料")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "客户类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索", dataType = "String", paramType = "query")
    })
    public CommResult<Company> list(Date startDate, Date endDate,String keyword,Integer status, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (startDate!=null){
           params.put("startDate", startDate);
        }
        if (endDate!=null){
           params.put("endDate", DateUtils.addMilliseconds(DateUtils.addDays(endDate,1),-1));
        }

        if (status!=null){
            params.put("status", status);
        }
        if (keyword!=null){
            params.put("keyword", keyword);
        }
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<Company> list = companyService.selectList(params);

        for (Company company:list) {
            SysUser sysUser = sysUserService.selectByPrimaryKey(company.getUserId());
            if (sysUser!=null) {
                company.setMobile(sysUser.getMobile());
            }
        }

        PageResult<Company> pageResult = new PageResult<Company>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条公司资料
     *
     * @return
    */
    @ApiOperation("查询单条公司资料")
    @GetMapping("/find/{id}")
    public CommResult<Company> find(@PathVariable String id) {
        Company result = companyService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("company",result);

        SysUser sysUser = sysUserService.selectByPrimaryKey(result.getUserId());
        if (sysUser!=null) {
            result.setMobile(sysUser.getMobile());
        }

        return CommResult.success(data);

    }


    /**
     * 保存公司资料
     *
     * @param company
     * @return
     */
    @ApiOperation("保存公司资料")
    @PostMapping("/save")
    public CommResult<Company> save(@RequestBody Company company,String mobile,String password) {

        SysUser sysUser = sysUserService.findByUsername("mch_10200_"+mobile);

        if (sysUser!=null) {
            return CommResult.error("账号已存在");
        }

        sysUser = new SysUser();
        sysUser.setCreateDate(new Date());
        sysUser.setUsername("mch_10200_"+mobile);
        sysUser.setDelFlag(false);
        sysUser.setStatus(SysUser.STATUS_ENABLED);
        sysUser.setUserType(2);

        sysUser.setMchId("10200");
        sysUser.setMobile(mobile);

        sysUser.setId(CodeGenerator.getUUID());

        sysUser.setPassword(MD5Utils.getMD5Str(password.trim() + sysUser.getUsername().trim()));

        int affectCount = sysUserService.insert(sysUser);
        if (affectCount <= 0) {
            return CommResult.error();
        }

        company.setUserId(sysUser.getId());
        company.setCreateDate(new Date());

        affectCount = companyService.insert(company);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Company result = companyService.findById(company.getId());
        return CommResult.success(result);

    }

    /**
     * 批量删除公司资料
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除公司资料", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除公司资料,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<Company> del(@PathVariable String ids) {

        companyService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新公司资料
     *
     * @param company
     * @return
     */
    @Log(desc = "更新公司资料", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新公司资料")
    @PostMapping("/update")
    public CommResult<Company> update(@RequestBody Company company) {
        int affectCount = companyService.updateByPrimaryKeySelective(company);
        if (affectCount <= 0){
            return CommResult.error();
        }
        Company result = companyService.findById(company.getId());

        SysUser sysUser = sysUserService.selectByPrimaryKey(result.getUserId());
        if (sysUser!=null) {
            result.setMobile(sysUser.getMobile());
        }

        return CommResult.success(result);
    }

}
