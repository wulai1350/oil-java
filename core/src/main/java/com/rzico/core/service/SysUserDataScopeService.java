/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-15
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysUserDataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysUserDataScopeMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 付款单业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysUserDataScopeService extends BaseServiceImpl<SysUserDataScope, String> {

    @Autowired
    private SysUserDataScopeMapper sysUserDataScopeMapper;

    @Override
    public BaseMapper<SysUserDataScope, String> getMapper() {
        return sysUserDataScopeMapper;
    }

    public Boolean checkHasPermissio(String userId,Integer scopeType) {

        Map<String,Object> params = new HashMap<>();
        params.put("scopeType",scopeType);
        params.put("userId",userId);
        return sysUserDataScopeMapper.selectRowCount(params)>0;

    }

    public Boolean checkHasShopPermissio(String userId,Long shopId) {

        Map<String,Object> params = new HashMap<>();
        params.put("scopeType",2);
        params.put("shopId",shopId);
        params.put("userId",userId);
        return sysUserDataScopeMapper.selectRowCount(params)>0;

    }


    public List<SysUserDataScope> getItemList(String userId) {

        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        return sysUserDataScopeMapper.selectList(params);

    }

}
