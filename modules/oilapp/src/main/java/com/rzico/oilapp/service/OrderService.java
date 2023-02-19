/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-13
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.OrderMapper;

/**
 * <pre>
 * 交易订单业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class OrderService extends BaseServiceImpl<Order, String> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WarehouseStockService warehouseStockService;

    @Override
    public BaseMapper<Order, String> getMapper() {
        return orderMapper;
    }

    //接单
    public void confirm(Order order) {
        order.setStatus(1);
        orderMapper.updateByPrimaryKeySelective(order);

    }

    //下订
    public void partialPayment(Order order) {
        order.setStatus(4);
        orderMapper.updateByPrimaryKeySelective(order);
        warehouseStockService.addOrder(order);

    }

    //付款
    public void payment(Order order) {
        order.setStatus(5);
        orderMapper.updateByPrimaryKeySelective(order);
        warehouseStockService.addOrder(order);

    }

    //关闭
    public void cancel(Order order) {
        order.setStatus(10);
        orderMapper.updateByPrimaryKeySelective(order);

    }

}
