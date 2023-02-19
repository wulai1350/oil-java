package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysOffice;
import com.rzico.core.mapper.SysOfficeMapper;
import com.rzico.util.StringUtils;
import com.rzico.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
*
<pre>
 * 系统角色业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysOfficeService extends BaseServiceImpl<SysOffice, String> {

    @Autowired
    private SysOfficeMapper sysOfficeMapper;

    @Autowired
    TreeUtils<SysOffice> treeUtils;

    @Override
    public BaseMapper<SysOffice, String> getMapper() {
        return sysOfficeMapper;
    }

    /**
     * 排序
     *
     * @param items
     *            排序
     * @return 排序
     */
    private List<SysOffice> sort(List<SysOffice> items) {
        List<SysOffice> result = new ArrayList<SysOffice>();
        if (items != null) {
            Collections.sort(items, new Comparator<SysOffice>() {
                public int compare(SysOffice n1, SysOffice n2) {
                    if (n1.getDataSort()==null) {
                        return -1;
                    }
                    if (n2.getDataSort()==null) {
                        return 1;
                    }
                    return n1.getDataSort().compareTo(n2.getDataSort());
                }
            });
            for (SysOffice item : items) {
                result.add(item);
                item.setChildrens(sort(item.getChildrens()));

            }
        }
        return result;
    }

    public List selectTree(Map<String, Object> params) throws Exception{
//        List<SysOffice> list2 = super.selectByExample(SysOffice.class,params);
//        return sort(treeUtils.buildTree(list));
        List<SysOffice> list = super.selectList(params);
        return buildTree(list);
    }

    /**
     * 生成treePath
     * @param sysOffice
     */
    public void setTreePath(SysOffice sysOffice){
        SysOffice parent = this.selectByPrimaryKey(sysOffice.getParentId());
        if (null != parent){
            sysOffice.setTreePath(parent.getTreePath() + sysOffice.getId() + SysOffice.TREE_PATH_SEPARATOR);
        }else{
            sysOffice.setTreePath(SysOffice.TREE_PATH_SEPARATOR + sysOffice.getId() + SysOffice.TREE_PATH_SEPARATOR);
        }
    }

    /**
     * 查找当前商户下当前节点的所有子节点
     * @param sysOffice
     * @return
     */
    public List<SysOffice> selectAllChildren(SysOffice sysOffice){
        return sysOfficeMapper.selectAllChildren(sysOffice);
    }

    @Override
    public int insert(SysOffice sysOffice) {
        if (StringUtils.isNotEmpty(sysOffice.getParentId())){
            setTreePath(sysOffice);
        }else{
            //根节点的treepath是自己的id
            sysOffice.setTreePath(SysOffice.TREE_PATH_SEPARATOR + sysOffice.getId() + SysOffice.TREE_PATH_SEPARATOR);
        }
        return super.insert(sysOffice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(SysOffice record) {
        //todo 查找当前节点的treepath
        if (null != record.getParentId()){
            setTreePath(record);
        }
        int num = super.updateByPrimaryKeySelective(record);
        if (num > 0){
            //更新子节点的treePath
            List<SysOffice> list = selectAllChildren(record);
            if (null != list && list.size() >0){
                for (SysOffice sysOffice : list){
                    setTreePath(sysOffice);
                    super.updateByPrimaryKeySelective(sysOffice);
                }
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(SysOffice record) {
        //todo 查找当前节点的treepath
        if (null != record.getParentId()){
            setTreePath(record);
        }
        int num = super.updateByPrimaryKey(record);
        if (num > 0){
            //更新子节点的treePath
            List<SysOffice> list = selectAllChildren(record);
            if (null != list && list.size() > 0){
                for (SysOffice sysOffice : list){
                    setTreePath(sysOffice);
                    super.updateByPrimaryKey(sysOffice);
                }
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Object[] ids) {
        int rw = 0;
        for (Object id:ids) {
            SysOffice sysOffice = selectByPrimaryKey(id);
            sysOffice.setDelFlag(true);
            rw = rw + super.updateByPrimaryKeySelective(sysOffice);
        }
        return rw;
    }

    public List<SysOffice> buildTree(List<SysOffice> list) {
        List<SysOffice> supers = list.stream().filter(sysOffice ->
                org.apache.commons.lang3.StringUtils.isEmpty(sysOffice.getParentId()))
                .collect(Collectors.toList());
        list.removeAll(supers);
        supers.sort(Comparator.comparingInt(SysOffice::getDataSort));
        List<SysOffice> resultList = new ArrayList<>();
        for (SysOffice sysOffice : supers) {
            SysOffice child = child(sysOffice, list, 0, 0);
            resultList.add(child);
        }
        return resultList;
    }

    public SysOffice child(SysOffice sysOffice, List<SysOffice> list, Integer pNum, Integer num) {
        List<SysOffice> childSysOffice = list.stream().filter(s ->
                s.getParentId().equals(sysOffice.getId())).collect(Collectors.toList());
        list.removeAll(childSysOffice);
        childSysOffice.sort(Comparator.comparingInt(SysOffice::getDataSort));
        List<SysOffice> voList = sysOffice.getChildrens();
        List<SysOffice> arrList = new ArrayList(voList);
        for (SysOffice office : childSysOffice) {
            ++num;
            child(office, list, pNum, num);

            arrList.add(office);
        }
        sysOffice.setChildrens(arrList);
        return sysOffice;
    }
}
