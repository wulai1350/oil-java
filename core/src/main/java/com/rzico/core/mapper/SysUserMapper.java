package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <pre>
 *   系统用户映射类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser,String> {

    SysUser login(@Param("username") String username);

    SysUser find(@Param("id") String id);

    int checkUserExists(String username);

}
