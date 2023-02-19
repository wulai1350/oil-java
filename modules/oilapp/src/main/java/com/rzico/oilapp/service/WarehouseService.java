/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.WarehouseMapper;

/**
 * <pre>
 * 油库管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class WarehouseService extends BaseServiceImpl<Warehouse, String> {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public BaseMapper<Warehouse, String> getMapper() {
        return warehouseMapper;
    }

}
