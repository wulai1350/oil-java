package com.rzico.rabbit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *   订单附加信息
 * Created by macro on 2018/5/17.
 */
@Data
public class Attach {

    @ApiModelProperty("任务:queue.order.payment,queue.order.refunds(对照sys_queue)")
    private String queue;
    @ApiModelProperty("单据 Id")
    private String id;
    @ApiModelProperty("卡/券Id")
    private String cardId;
    @ApiModelProperty("商品Id")
    private Long productId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("单据描述")
    private String description;
    @ApiModelProperty("响应码 0000为成功，其他是失败")
    private String code;

}
