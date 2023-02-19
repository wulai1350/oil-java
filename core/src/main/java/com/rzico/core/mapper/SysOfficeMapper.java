package com.rzico.core.mapper;

import com.rzico.base.BaseMapper;
import com.rzico.core.entity.SysOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Mapper
public interface SysOfficeMapper extends BaseMapper<SysOffice, String> {

    public List<SysOffice> selectAllChildren(SysOffice sysOffice);

}