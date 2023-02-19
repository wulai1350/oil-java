package com.rzico.article.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 *   订单附加信息
 * Created by macro on 2018/5/17.
 */
@Data
public class Meta {
    @ApiModelProperty("媒体类型（1.图片,2.视频）")
    private String type;
    @ApiModelProperty("资源地址")
    private String url;
}
