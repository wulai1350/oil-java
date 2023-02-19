/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-11
*/
package com.rzico.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rzico.annotation.IgnoreSwaggerParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <pre>
 *   人员表领域对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
@ApiModel(description= "员工信息")
@JsonIgnoreProperties(value = {"handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysEmployee {

    /**
     * 员工编码
     */
    @Id
    protected String id;
    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    protected String name;

    /**
     * 员工号
     */
    protected String empNo;

    /**
     * 部门编码
     */
    protected String officeId;
    /**
     * 商户号
     */
    protected String mchId;
    /**
     * 绑定手机号
     */
    protected String phone;
    /**
     * 状态（1在职 0离职）
     */
    protected Integer status;

    /**
     * 绑定用户
     */
    protected String userId;

    /**
     * 备注信息
     */
    protected String remark;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    protected String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    protected java.util.Date createDate;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    protected String modifyBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    protected java.util.Date modifyDate;
    /**
     * 删除标志
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected Boolean delFlag;

    /**
     * 部门
     */
    @JsonIgnore
    @IgnoreSwaggerParameter
    protected SysOffice sysOffice;

    @JsonIgnore
    protected List<SysPost> postList = Collections.emptyList();


    @ApiModelProperty(value = "名称", hidden = true)
    public String getOfficeName(){
        SysOffice sysOffice1 = getSysOffice();
        if (null != sysOffice1){
            return sysOffice1.getName();
        }else{
            return "";
        }
    }

    public void setOfficeName(String officeName){

    }

}
