/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysJobMapper;

/**
 * <pre>
 * 计划任务表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysJobService extends BaseServiceImpl<SysJob, String> {

    @Autowired
    private SysJobMapper sysJobMapper;

    @Override
    public BaseMapper<SysJob, String> getMapper() {
        return sysJobMapper;
    }

}
