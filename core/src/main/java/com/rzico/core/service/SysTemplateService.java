/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.CommResult;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysTemplate;
import com.rzico.util.FreemarkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysTemplateMapper;

import java.util.Map;

/**
 * <pre>
 * 文件表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysTemplateService extends BaseServiceImpl<SysTemplate, String> {

    @Autowired
    private SysTemplateMapper sysTemplateMapper;

    @Override
    public BaseMapper<SysTemplate, String> getMapper() {
        return sysTemplateMapper;
    }

    public String encodeMsg(SysTemplate sysTemplate,Map<String,Object> data) {
        String template = sysTemplate.getTplContent();
        String msg = "";
        try {
            msg = FreemarkerUtils.process(template, data);
            return msg;
        } catch (Exception e) {
            return null;
        }
    }

    public SysTemplate findByTplKey(String mchId,String tplKey) {
        SysTemplate sysTemplate = new SysTemplate();
        sysTemplate.setMchId(mchId);
        sysTemplate.setTplKey(tplKey);
        SysTemplate template = super.selectOne(sysTemplate);
        return template;
    }

}
