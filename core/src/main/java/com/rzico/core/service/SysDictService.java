/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-16
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysDict;
import com.rzico.core.entity.SysPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysDictMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 数据字典业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysDictService extends BaseServiceImpl<SysDict, String> {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public BaseMapper<SysDict, String> getMapper() {
        return sysDictMapper;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysDict sysDict = selectByPrimaryKey(id);
            sysDict.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysDict);
        }
        return rw;
    }

}
