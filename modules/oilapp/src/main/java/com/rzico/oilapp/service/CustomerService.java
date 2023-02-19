/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Company;
import com.rzico.oilapp.entity.Customer;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.CustomerMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 客户资料业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class CustomerService extends BaseServiceImpl<Customer, String> {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public BaseMapper<Customer, String> getMapper() {
        return customerMapper;
    }

    public Boolean checkExists(Customer customer) {
        if (customer.getName()==null) {
            return false;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("name",customer.getName());
        if (customer.getId()!=null) {
            params.put("noid",customer.getId());
        }
        params.put("companyId",customer.getCompanyId());
        Integer w= customerMapper.selectRowCount(params);
        return w>0;
    }

}
