/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysMchMenu;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysMchMenuMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 文件表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysMchMenuService extends BaseServiceImpl<SysMchMenu, String> {

    @Autowired
    private SysMchMenuMapper sysMchMenuMapper;

    @Override
    public BaseMapper<SysMchMenu, String> getMapper() {
        return sysMchMenuMapper;
    }

//    @Transactional(rollbackFor = Exception.class)
//    public int addOrUpdateMchMenu(String mchId, String[] menus){
//        int rw = 0;
//        SysMchMenu sysMchMenu = new SysMchMenu();
//        sysMchMenu.setMchId(mchId);
//        sysMchMenuMapper.delete(sysMchMenu);
//        for (String menuId : menus){
//            SysMchMenu mchMenu = new SysMchMenu();
//            mchMenu.setId(CodeGenerator.getUUID());
//            mchMenu.setMchId(mchId);
//            mchMenu.setMenuId(menuId);
//            rw += sysMchMenuMapper.insert(mchMenu);
//        }
//        return rw;
//    }


}
