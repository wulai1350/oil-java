/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysOffice;
import com.rzico.core.entity.SysPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysPostMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 职位表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysPostService extends BaseServiceImpl<SysPost, String> {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public BaseMapper<SysPost, String> getMapper() {
        return sysPostMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysPost sysPost = selectByPrimaryKey(id);
            sysPost.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysPost);
        }
        return rw;
    }
}
