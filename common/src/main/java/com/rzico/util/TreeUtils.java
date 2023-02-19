package com.rzico.util;

import com.rzico.annotation.Log;
import org.apache.commons.lang3.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 封装通用树形工具类
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-13
 */
@Component
public class TreeUtils<T> {

    protected final String TREE_PATH_SEPARATOR = ",";

    protected final String FIELD_TREE_PATH = "treePath";

    protected final String FIELD_ID = "id";

    protected final String FIELD_CHILDREN = "childrens";

    private static final String STR = "java.lang.String";

    private static final String LONG = "java.lang.Long";
    /**
     * 构造树形，传入的list必须按treePath asc 排序
     * @param list
     * @return
     */
    public List<T> buildTree(List<T> list) throws NoSuchFieldException, IllegalAccessException{
        //根节点的下标
        int rootIndex = 0;
        //父节点的下标
        int parentIndex = 0;
        //当前节点的下标
        int currentIndex = 0;
        List<T> result = new ArrayList<>();
        Object[] obj = new Object[20];
        for (int i = 0; i < list.size(); i++){
            T record = list.get(i);
            Class<?> clazz = record.getClass();
            Field treePathField = clazz.getDeclaredField(FIELD_TREE_PATH);
            treePathField.setAccessible(true);
            String treePathVal = (String) treePathField.get(record);
            currentIndex = treePathVal.split(TREE_PATH_SEPARATOR).length;
            //TODO 根节点处理
            if (i == 0 ){
                rootIndex = treePathVal.split(TREE_PATH_SEPARATOR).length;
                obj[rootIndex] = record;
                result.add(record);
            } else {
                T rootRecord = (T)obj[rootIndex];
                Class<?> root = rootRecord.getClass();
                Field idField = root.getDeclaredField(FIELD_ID);
                idField.setAccessible(true);
                String idVal = "," + String.valueOf(idField.get(rootRecord)) + ",";
                //当前对象的tree path不包含根节点的ID，即为新的根节点
                if (!treePathVal.contains(idVal)){
                    rootIndex = treePathVal.split(TREE_PATH_SEPARATOR).length;
                    obj[rootIndex] = record;
                    result.add(record);
                }else{
                    //TODO 子节点处理
                    parentIndex = treePathVal.split(TREE_PATH_SEPARATOR).length -1;
                    obj[currentIndex] = record;
                    T parentRecord = (T)obj[parentIndex];
                    Class<?> parentClazz = parentRecord.getClass();
                    Field childrenField = parentClazz.getDeclaredField(FIELD_CHILDREN);
                    childrenField.setAccessible(true);
                    List<T> childList = (List) childrenField.get(parentRecord);
                    List<T> recordList = new ArrayList<>(childList);
                    recordList.add(record);
                    childrenField.set(parentRecord, recordList);
                }
            }
        }
        return result;
    }


/*    public List<T> buildTree2(List<T> list) throws NoSuchFieldException{
        for (int i = 0; i < list.size(); i++) {
            T record = list.get(i);
            Class<?> clazz = record.getClass();
        }

        List<T> supers = list.stream().filter(T t ->{
            Class<?> clazz =
            Field parentIdField = clazz.getDeclaredField("parentId");
            parentIdField.setAccessible(true);
            String parentId = (String) parentIdField.get(record);
            StringUtils.isEmpty(parentId);}.collect(Collectors.toList()));
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
    }*/
}
