package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *   系统菜单映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu,String> {

    List<SysMenu> selectAllChildren(SysMenu sysMenu);

}
