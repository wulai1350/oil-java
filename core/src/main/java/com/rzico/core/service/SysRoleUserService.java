/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysRoleUserMapper;

/**
 * <pre>
 * 用户角色业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysRoleUserService extends BaseServiceImpl<SysRoleUser, String> {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public BaseMapper<SysRoleUser, String> getMapper() {
        return sysRoleUserMapper;
    }

}
