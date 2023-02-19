/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
/**
 * <pre>
 *   文章管理映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article, String> {
   List<Article> selectList(Map<String, Object> params);
}
