/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-08-28
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysBindUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysBindUserMapper;

import java.util.List;

/**
 * <pre>
 * 绑定用户业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysBindUserService extends BaseServiceImpl<SysBindUser, String> {

    @Autowired
    private SysBindUserMapper sysBindUserMapper;

    @Override
    public BaseMapper<SysBindUser, String> getMapper() {
        return sysBindUserMapper;
    }


    public SysBindUser bindUser(String userId,String bindId) {
        SysBindUser sysBindUser = new SysBindUser();
        sysBindUser.setBindType(0);
        sysBindUser.setUserId(userId);
        sysBindUser.setBindId(bindId);
        List<SysBindUser> sysBindUserList = sysBindUserMapper.select(sysBindUser);
        if (sysBindUserList.size()==0) {
            sysBindUserMapper.insert(sysBindUser);
        }
        return sysBindUser;
    }



    public Boolean checkBindUser(String userId,String bindId) {
        SysBindUser sysBindUser = new SysBindUser();
        sysBindUser.setBindType(0);
        sysBindUser.setUserId(userId);
        sysBindUser.setBindId(bindId);
        List<SysBindUser> sysBindUserList = sysBindUserMapper.select(sysBindUser);
        if (sysBindUserList.size()>0) {
            return true;
        }
        return false;
    }

}
