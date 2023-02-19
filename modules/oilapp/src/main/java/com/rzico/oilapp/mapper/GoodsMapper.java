/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-01-16
*/
package com.rzico.oilapp.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.oilapp.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   商品库分类映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods, String> {
   List<Goods> selectList(Map<String, Object> params);
   int selectRowCount(Map<String, Object> params);
}
