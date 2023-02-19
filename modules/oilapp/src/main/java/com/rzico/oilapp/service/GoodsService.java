/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-16
*/
package com.rzico.oilapp.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.oilapp.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.oilapp.mapper.GoodsMapper;

/**
 * <pre>
 * 商品库分类业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class GoodsService extends BaseServiceImpl<Goods, String> {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public BaseMapper<Goods, String> getMapper() {
        return goodsMapper;
    }

}
