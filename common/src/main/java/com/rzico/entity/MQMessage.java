package com.rzico.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by zhangsr on 2020/5/28.
 */
@Data
public class MQMessage {
    @ApiModelProperty("任务")
    private String queue;
    @ApiModelProperty("单据 Id")
    private String id;
    @ApiModelProperty("响应码 0000为成功，其他是失败")
    private String code;
}
