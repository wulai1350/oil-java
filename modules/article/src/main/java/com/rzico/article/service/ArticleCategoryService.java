/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-03-23
*/
package com.rzico.article.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.article.entity.ArticleCategory;
import com.rzico.exception.CustomException;
import com.rzico.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.article.mapper.ArticleCategoryMapper;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 文章分类业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class ArticleCategoryService extends BaseServiceImpl<ArticleCategory, String> {

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    TreeUtils<ArticleCategory> treeUtils;

    @Override
    public BaseMapper<ArticleCategory, String> getMapper() {
        return articleCategoryMapper;
    }

    /**
     *
     * @param params
     * @return
     */
    public List<ArticleCategory> selectTree(Map<String, Object> params) {
        Example example = new Example(ArticleCategory.class);
        criteriaBuilderMap(example, params);
        List<ArticleCategory> list = articleCategoryMapper.selectByExample(example);
        try {
            return treeUtils.buildTree(list);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUseGeneratedKeys(ArticleCategory mchGaugeCategory) {
        //数据库字段存储不能为空，先存个逗号
        mchGaugeCategory.setTreePath(",");
        int num = super.insertUseGeneratedKeys(mchGaugeCategory);
        if (num > 0){
            if (null != mchGaugeCategory.getParentId() && 0 != mchGaugeCategory.getParentId()){
                setTreePath(mchGaugeCategory);
            }else{
                mchGaugeCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR + mchGaugeCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
            }
            updateByPrimaryKeySelective(mchGaugeCategory);
        }
        return num;
    }

    /**
     * 生成treePath
     * @param mchGaugeCategory
     */
    public void setTreePath(ArticleCategory mchGaugeCategory){
        ArticleCategory parent = this.selectByPrimaryKey(mchGaugeCategory.getParentId());
        if (null != parent){
            mchGaugeCategory.setTreePath(parent.getTreePath() + mchGaugeCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
        }else{
            mchGaugeCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR + mchGaugeCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(ArticleCategory record) {
        //todo 查找当前节点的treepath
        if (null != record.getParentId()){
            setTreePath(record);
        }
        int num = super.updateByPrimaryKeySelective(record);
        if (num > 0){
            //更新子节点的treePath
            List<ArticleCategory> list = articleCategoryMapper.selectAllChildren(record);
            if (null != list && list.size() > 0){
                for (ArticleCategory mchGaugeCategory : list){
                    setTreePath(mchGaugeCategory);
                    super.updateByPrimaryKeySelective(mchGaugeCategory);
                }
            }
        }
        return num;
    }


}
