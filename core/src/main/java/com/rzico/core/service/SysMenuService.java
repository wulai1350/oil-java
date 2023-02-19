package com.rzico.core.service;

import com.alibaba.fastjson.JSONArray;
import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysOffice;
import com.rzico.core.mapper.SysMenuMapper;
import com.rzico.core.mapper.SysRoleMenuMapper;
import com.rzico.exception.CustomException;
import com.rzico.util.TreeUtils;
import com.rzico.core.entity.SysRoleMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
*
<pre>
 * 系统菜单业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysMenuService extends BaseServiceImpl<SysMenu, String> {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private TreeUtils<SysMenu> treeUtils;

    @Override
    public BaseMapper<SysMenu, String> getMapper() {
        return sysMenuMapper;
    }

    /**
     * 排序
     *
     * @param items
     *            排序
     * @return 排序
     */
    private List<SysMenu> sort(List<SysMenu> items) {
        List<SysMenu> result = new ArrayList<SysMenu>();
        if (items != null) {
            Collections.sort(items, new Comparator<SysMenu>() {
                public int compare(SysMenu n1, SysMenu n2) {
                    if (n1.getDataSort()==null) {
                        return -1;
                    }
                    if (n2.getDataSort()==null) {
                        return 1;
                    }
                    return n1.getDataSort().compareTo(n2.getDataSort());
                }
            });
            for (SysMenu item : items) {
                result.add(item);
                item.setChildrens(sort(item.getChildrens()));

            }
        }
        return result;
    }

    public List<SysMenu> treeList(Integer menuType, String menuId){
        List<SysMenu> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("sortField", "tree_path");
        params.put("sortType", "ASC");
        if (menuType == 0){
            params.put("menuType", menuType);
        }
        if (StringUtils.isNotEmpty(menuId)){
            params.put("treePath", menuId);
        }
//        List<SysMenu> list =  sysMenuMapper.selectList(params);
        List<SysMenu> list = super.selectByExample(SysMenu.class,params);
        Assert.notNull(list, "尚未查找到菜单数据");
        try {
            result = sort(treeUtils.buildTree(list));
        } catch (Exception e){
            e.printStackTrace();
            throw new CustomException("构造菜单树形异常");
        }
        return result;
    }



    /**
     * 生成treePath
     * @param sysMenu
     */
    public void setTreePath(SysMenu sysMenu){
        SysMenu parent = this.selectByPrimaryKey(sysMenu.getParentId());
        if (null != parent){
            sysMenu.setTreePath(parent.getTreePath() + sysMenu.getId() + SysMenu.TREE_PATH_SEPARATOR);
        }else{
            sysMenu.setTreePath(SysMenu.TREE_PATH_SEPARATOR + sysMenu.getId() + SysMenu.TREE_PATH_SEPARATOR);
        }
    }

    /**
     * 查找当前商户下当前节点的所有子节点
     * @param sysMenu
     * @return
     */
    public List<SysMenu> selectAllChildren(SysMenu sysMenu){
        return sysMenuMapper.selectAllChildren(sysMenu);
    }

    @Override
    public int insert(SysMenu sysMenu) {
        if (StringUtils.isNotEmpty(sysMenu.getParentId())){
            setTreePath(sysMenu);
        }else{
            //根节点的treepath是自己的id
            sysMenu.setTreePath(SysMenu.TREE_PATH_SEPARATOR + sysMenu.getId() + SysMenu.TREE_PATH_SEPARATOR);
        }
        return super.insert(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(SysMenu record) {
        //todo 查找当前节点的treepath
        if (null != record.getParentId()){
            setTreePath(record);
        }
        int num = super.updateByPrimaryKeySelective(record);
        if (num > 0){
            //更新子节点的treePath
            List<SysMenu> list = selectAllChildren(record);
            if (null != list && list.size() >0){
                for (SysMenu sysMenu : list){
                    setTreePath(sysMenu);
                    super.updateByPrimaryKeySelective(sysMenu);
                }
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(SysMenu record) {
        //todo 查找当前节点的treepath
        if (null != record.getParentId()){
            setTreePath(record);
        }
        int num = super.updateByPrimaryKey(record);
        if (num > 0){
            //更新子节点的treePath
            List<SysMenu> list = selectAllChildren(record);
            if (null != list && list.size() > 0){
                for (SysMenu sysMenu : list){
                    setTreePath(sysMenu);
                    super.updateByPrimaryKey(sysMenu);
                }
            }
        }
        return num;
    }


    public List<SysMenu> buildTree(List<SysMenu> list) {
        List<SysMenu> supers = list.stream().filter(sysMenu ->
                StringUtils.isEmpty(sysMenu.getParentId()))
                .collect(Collectors.toList());
        list.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysMenu::getDataSort));
        List<SysMenu> resultList = new ArrayList<>();
        for (SysMenu sysMenu : supers) {
            SysMenu child = child(sysMenu, list, 0, 0);
            resultList.add(child);
        }
        return resultList;
    }

    public SysMenu child(SysMenu sysMenu, List<SysMenu> list, Integer pNum, Integer num) {
        List<SysMenu> childSysMenu = list.stream().filter(s ->
                s.getParentId().equals(sysMenu.getId())).collect(Collectors.toList());
        list.removeAll(childSysMenu);
        childSysMenu.sort(Comparator.comparingInt(SysMenu::getDataSort));
        SysMenu m;
        for (SysMenu menu : childSysMenu) {
            ++num;
            m = child(menu, list, pNum, num);
            sysMenu.getChildrens().add(menu);
        }
        return sysMenu;
    }
}
