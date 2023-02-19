package com.rzico.oilapp.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *   订单附加信息
 * Created by macro on 2018/5/17.
 */
@Data
public class Meta {

    @ApiModelProperty("营业执照")
    private String license;
    @ApiModelProperty("批发经营证书")
    private String wholesale;
    @ApiModelProperty("危险化学品经营许可证")
    private String chemical;
    @ApiModelProperty("其他证件")
    private String other;
    @ApiModelProperty("开户许可证")
    private String accountLicense;
    @ApiModelProperty("身份证正面")
    private String legalPerson1;
    @ApiModelProperty("身份证反面")
    private String legalPerson2;
    @ApiModelProperty("法人代表人授权书")
    private String legalGrant;

    //以上是图，以下是内容

    @ApiModelProperty("统一社会信用代码")
    private String licenseCode;
    @ApiModelProperty("身份证号")
    private String legalPersonCode;
    @ApiModelProperty("开户行")
    private String bankname;
    @ApiModelProperty("开户行账号")
    private String banknum;


}
