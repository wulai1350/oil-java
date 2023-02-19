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
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.service.CompanyService;
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
 * 公司资料控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2023-01-13
 */
@Api(description = "公司资料接口")
@RestController
@RequestMapping("/member/company")
public class CompanyMemberController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询当前账号公司信息
     *
     * @return
    */
    @ApiOperation("查询当前账号公司信息")
    @GetMapping("/getCurrent")
    public CommResult<Company> getCurrent() {

        Company company = companyService.getCurrent();

        Map<String,Object> data = new HashMap<>();
        data.put("company",company);

        SysUser sysUser = sysUserService.selectByPrimaryKey(company.getUserId());
        if (sysUser!=null) {
            company.setMobile(sysUser.getMobile());
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
    public CommResult<Company> save(@RequestBody Company company) {

        SysUser sysUser = sysUserService.getCurrent();

        if (sysUser==null) {
            return CommResult.error("没有登录");
        }

        company.setUserId(sysUser.getId());
        company.setCreateDate(new Date());

        int affectCount = companyService.insert(company);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        Company result = companyService.findById(company.getId());
        return CommResult.success(result);

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
