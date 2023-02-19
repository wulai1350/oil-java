/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.exception.CustomException;
import com.rzico.oilapp.entity.Order;
import com.rzico.oilapp.entity.Shipping;
import com.rzico.oilapp.entity.WarehouseStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.WarehouseStockMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 库存管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class WarehouseStockService extends BaseServiceImpl<WarehouseStock, String> {

    @Autowired
    private WarehouseStockMapper warehouseStockMapper;

    @Override
    public BaseMapper<WarehouseStock, String> getMapper() {
        return warehouseStockMapper;
    }

    private WarehouseStock initOrder(Order order) {
        Map<String,Object> params = new HashMap<>();
        params.put("productId",order.getProductId());
        params.put("warehouseId",order.getWarehouseId());
        params.put("customerId",order.getCustomerId());
        List<WarehouseStock> warehouseStockList = warehouseStockMapper.selectList(params);
        WarehouseStock warehouseStock = null;
        if (warehouseStockList.size()==0) {
            warehouseStock = new WarehouseStock();
            warehouseStock.setCompanyId(order.getCompanyId());
            warehouseStock.setCustomerId(order.getCustomerId());
            warehouseStock.setSupplierId(order.getSupplierId());
            warehouseStock.setWarehouseId(order.getWarehouseId());
            warehouseStock.setProductId(order.getProductId());
            warehouseStock.setStock(BigDecimal.ZERO);
            warehouseStock.setAllocateStock(BigDecimal.ZERO);
            warehouseStock.setCreateDate(new Date());
            insertUseGeneratedKeys(warehouseStock);
        } else {
            warehouseStock = warehouseStockList.get(0);
        }
        return warehouseStock;
    }

    private WarehouseStock initShipping(Shipping shipping) {
        Map<String,Object> params = new HashMap<>();
        params.put("productId",shipping.getProductId());
        params.put("warehouseId",shipping.getWarehouseId());
        params.put("customerId",shipping.getCustomerId());
        List<WarehouseStock> warehouseStockList = warehouseStockMapper.selectList(params);
        if (warehouseStockList.size()==0) {
           throw new CustomException("没有找到库存");
        }
        return warehouseStockList.get(0);
    }

    public void addOrder(Order order) {
        WarehouseStock warehouseStock = initOrder(order);
        if (order.getStatus().equals(4)) {
            warehouseStock.setAllocateStock(warehouseStock.getAllocateStock().add(order.getQuantity()));
            updateByPrimaryKeySelective(warehouseStock);
        }
        if (order.getStatus().equals(5)) {
            warehouseStock.setAllocateStock(warehouseStock.getAllocateStock().subtract(order.getQuantity()));
            warehouseStock.setStock(warehouseStock.getStock().add(order.getQuantity()));
            updateByPrimaryKeySelective(warehouseStock);
        }
    }

    public void addShipping(Shipping shipping) {
        WarehouseStock warehouseStock = initShipping(shipping);

        warehouseStock.setStock(warehouseStock.getStock().subtract(shipping.getQuantity()));
        updateByPrimaryKeySelective(warehouseStock);

    }

}
