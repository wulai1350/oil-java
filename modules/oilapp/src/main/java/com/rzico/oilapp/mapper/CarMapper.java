/**
* 版权声明：睿商(厦门)科技股份有限公司 版权所有 违者必究
* 日    期：2023-02-14
*/
package com.rzico.oilapp.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.oilapp.entity.Car;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   车辆管理映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface CarMapper extends BaseMapper<Car, String> {
   List<Car> selectList(Map<String, Object> params);
   int selectRowCount(Map<String, Object> params);
}
