/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-04
*/
package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * <pre>
 *   插件管理数据库对象
 * </pre>
 * @author zhongzm
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPlugin{

    public static Integer STORAGEPLUGIN=1;
    public static Integer PAYMENTPLUGIN=2;
    public static Integer MSGPLUGIN=3;
    public static Integer AUTHPLUGIN=4;
    public static Integer TRANSFERPLUGIN=5;
    public static Integer SHAREPLUGIN=6;

    //编号
    @Id
    protected String id;
    //插件名称
    protected String name;
    //插件版本
    protected String version;
    //插件编号
    protected String pluginId;
    //插件类型（1.存储插件 2.支付插件 3手机短信 4.授权插件 5.付款插件,6.分账插件,7.分销插件,8.物流插件）
    protected Integer pluginType;
    //是否安装
    protected Boolean isInstalled;
    //是否启用
    protected Boolean isEnabled;
    //商户号
    protected String mchId;
    //创建者
    protected String createBy;
    //创建时间
    protected java.util.Date createDate;
    //更新者
    protected String modifyBy;
    //更新时间
    protected java.util.Date modifyDate;

    @JsonIgnore
    protected List<SysPluginAttribute> sysPluginAttribute = Collections.emptyList();

    @JsonIgnore
    public String getAttribute(String name) {
        List<SysPluginAttribute> attributes = getSysPluginAttribute();
        for (SysPluginAttribute sysPluginAttribute:attributes) {
            if (sysPluginAttribute.getName().equals(name)) {
                return sysPluginAttribute.getAttribute();
            }
        }
        return null;
    }

    @JsonIgnore
    public void setAttribute(String name,String value) {
        List<SysPluginAttribute> attributes = getSysPluginAttribute();
        for (SysPluginAttribute sysPluginAttribute:attributes) {
            if (sysPluginAttribute.getName().equals(name)) {
                sysPluginAttribute.setAttribute(value);
                break;
            }
        }
    }
}
