/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-05
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysIconMapper;

/**
 * <pre>
 * 设计器业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysIconService extends BaseServiceImpl<SysIcon, String> {

    @Autowired
    private SysIconMapper sysIconMapper;

    @Override
    public BaseMapper<SysIcon, String> getMapper() {
        return sysIconMapper;
    }

}
