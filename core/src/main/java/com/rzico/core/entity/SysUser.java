package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.*;

/**
* <pre>
 *   系统用户领域对象
 * </pre>
* @author Rzico Boot
* @version 1.0
*/
@Data
@ApiModel(description= "用户信息")
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUser implements Serializable{

    /**
     * 状态.启用
     */
    public static Integer STATUS_ENABLED=1;

    /**
     * 状态.禁用
     */
    public static Integer STATUS_DISBALED=2;

    /**
     * 状态.冻结
     */
    public static Integer STATUS_FREEZE=3;

    @Id
    protected String id;

    @ApiModelProperty(value = "账号")
    protected String username;

    @JsonIgnore
    @ApiModelProperty(value = "用户类型（1平台账号 2商户账号）")
    protected Integer userType;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    protected String password;

    @JsonIgnore
    protected String capitalPassword;

    @ApiModelProperty(value = "手机号码")
    protected String mobile;

    @ApiModelProperty(value = "绑定邮箱")
    protected String email;

    @JsonIgnore
    protected Boolean delFlag;

    protected String avatar;

    protected String nickname;

    @ApiModelProperty(value = "状态（1正常 2停用 3冻结）")
    protected Integer status;

    @JsonIgnore
    @ApiModelProperty(value = "归属服务端及普通商户")
    protected String mchId;

    @JsonIgnore
    @ApiModelProperty(value = "微信公众号")
    protected String wxId;

    @JsonIgnore
    @ApiModelProperty(value = "微博授权")
    protected String wbId;

    @JsonIgnore
    @ApiModelProperty(value = "QQ授权")
    protected String qqId;

    @JsonIgnore
    @ApiModelProperty(value = "微信小程序")
    protected String wxmId;

    @JsonIgnore
    @ApiModelProperty(value = "设备标识号")
    protected String imeiId;

    @ApiModelProperty(value = "推荐人(限做私域流量商户)")
    protected String referrer;

    //
    @JsonIgnore
    protected String createBy;
    //
    @JsonIgnore
    protected String modifyBy;
    //
    @JsonIgnore
    protected Date createDate;
    //
    @JsonIgnore
    protected Date modifyDate;
    //
    protected String remark;
    //
    protected String lastLoginIp;

    protected Date lastLoginDate;

    protected Date freezeDate;

    protected String freezeCause;

    @JsonIgnore
    protected List<SysRole> roleList = Collections.emptyList();

    @JsonIgnore
    protected List<SysOffice> officeList = Collections.emptyList();

    @JsonIgnore
    protected List<SysMenu> menuList = Collections.emptyList();

    public List<String> getRoleName() {
        List<String> roles = new ArrayList<>();
        for (SysRole sysRole:roleList) {
            roles.add(sysRole.getName());
        }
        return roles;
    }

    public void setRoleName(List<String> roleName){

    }

}
