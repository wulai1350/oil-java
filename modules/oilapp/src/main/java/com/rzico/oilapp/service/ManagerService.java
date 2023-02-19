/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.ManagerMapper;

/**
 * <pre>
 * 操作日期业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class ManagerService extends BaseServiceImpl<Manager, String> {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public BaseMapper<Manager, String> getMapper() {
        return managerMapper;
    }

}
