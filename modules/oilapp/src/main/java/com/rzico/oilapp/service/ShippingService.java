/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Order;
import com.rzico.oilapp.entity.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.ShippingMapper;

/**
 * <pre>
 * 提货管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class ShippingService extends BaseServiceImpl<Shipping, String> {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private WarehouseStockService warehouseStockService;

    @Override
    public BaseMapper<Shipping, String> getMapper() {
        return shippingMapper;
    }

    //预约
    public void confirm(Shipping shipping) {
        shipping.setStatus(1);
        shippingMapper.updateByPrimaryKeySelective(shipping);

    }

    //提货
    public void shipping(Shipping shipping) {
        shipping.setStatus(2);
        shippingMapper.updateByPrimaryKeySelective(shipping);

        warehouseStockService.addShipping(shipping);

    }


    //关闭
    public void cancel(Shipping shipping) {
        shipping.setStatus(10);
        shippingMapper.updateByPrimaryKeySelective(shipping);

    }
}
