/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-20
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysArea;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysOffice;
import com.rzico.entity.Pageable;
import com.rzico.util.StringUtils;
import com.rzico.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysAreaMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 区域管理业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysAreaService extends BaseServiceImpl<SysArea, String> {

    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Autowired
    private TreeUtils<SysArea> treeUtils;

    @Override
    public BaseMapper<SysArea, String> getMapper() {
        return sysAreaMapper;
    }


    public List<SysArea> selectTree() throws NoSuchFieldException, IllegalAccessException{
        List<SysArea> list = sysAreaMapper.selectAll();
        return treeUtils.buildTree(list);
    }

    /**
     * 递归，建立树形结构
     * @param list
     * @return
     */
    private List buildTree(List<SysArea> list){
        List treeList = new ArrayList();
        for (SysArea sysArea : getRootNode(list)) {
            sysArea=buildChildTree(sysArea, list);
            treeList.add(sysArea);
        }
        return treeList;
    }

    /**
     * 递归，建立子树形结构
     * @param rootArea
     * @param list
     * @return
     */
    private SysArea buildChildTree(SysArea rootArea, List<SysArea> list){
        List<SysArea> childList = new ArrayList();
        for (SysArea sysArea : list) {
            if (sysArea.getParentId().equals(rootArea.getId())) {
                childList.add(buildChildTree(sysArea, list));
            }
        }
        rootArea.setChildrens(childList);
        return rootArea;
    }

    private List<SysArea> getRootNode(List<SysArea> list){
        List<SysArea> rootList = new ArrayList<SysArea>();
        for (SysArea sysArea : list){
            if (sysArea.getParentId().equalsIgnoreCase("1")){
                rootList.add(sysArea);
            }
        }
        return rootList;
    }
}
