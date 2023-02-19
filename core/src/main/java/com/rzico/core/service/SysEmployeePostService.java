/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysEmployeePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysEmployeePostMapper;

/**
 * <pre>
 * 人员职位表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysEmployeePostService extends BaseServiceImpl<SysEmployeePost, String> {

    @Autowired
    private SysEmployeePostMapper sysEmployeePostMapper;

    @Override
    public BaseMapper<SysEmployeePost, String> getMapper() {
        return sysEmployeePostMapper;
    }

}
