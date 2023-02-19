/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.article.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.article.mapper.ArticleMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 文章管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class ArticleService extends BaseServiceImpl<Article, String> {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public BaseMapper<Article, String> getMapper() {
        return articleMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Article save(Article article) {

        article.setCreateDate(new Date());

        article.setDeleted(false);
        super.insertUseGeneratedKeys(article);

        return article;
    }


    @Transactional(rollbackFor = Exception.class)
    public Article update(Article article) {

        super.updateByPrimaryKeySelective(article);

        return article;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            Article article = selectByPrimaryKey(id);
            article.setDeleted(true);
            rw = rw + super.updateByPrimaryKeySelective(article);
        }
        return rw;
    }


    @Transactional(rollbackFor = Exception.class)
    public void hits(Article article,Long companyId,String openId) {

        if (article.getHits()==null) {
            article.setHits(0L);
        }

        article.setHits(article.getHits()+1);
        super.updateByPrimaryKeySelective(article);

    }


}
