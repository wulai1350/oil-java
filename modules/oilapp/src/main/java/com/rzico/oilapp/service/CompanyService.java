/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.CommResult;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.oilapp.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.CompanyMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 公司资料业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class CompanyService extends BaseServiceImpl<Company, String> {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public BaseMapper<Company, String> getMapper() {
        return companyMapper;
    }

    public Company getCurrent() {

        SysUser sysUser = sysUserService.getCurrent();

        Map<String,Object> params = new HashMap<>();
        params.put("userId",sysUser.getId());

        List<Company> companyList = companyMapper.selectList(params);
        if (companyList.size()>0) {
            return companyList.get(0);
        } else {
            return register(sysUser);
        }
    }

    private Company register(SysUser sysUser) {
        Company company = new Company();
        company.setUserId(sysUser.getId());
        company.setLinkman(sysUser.getMobile());
        company.setName(sysUser.getNickname());
        company.setType(1);
        company.setPhone(sysUser.getMobile());
        company.setCreateDate(new Date());
        company.setStatus(0);
        companyMapper.insertUseGeneratedKeys(company);
        return company;
    }

}
