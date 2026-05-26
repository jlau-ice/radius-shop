package com.ice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private LocalDateTime payTime;
    private String payTransactionId;
    private String deliveryAddress;
    private String deliveryContact;
    private String deliveryPhone;
    private Long deliveryPersonId;
    private LocalDateTime deliveryTime;
    private String deliveryPhoto;
    private String remark;
    private String cancelReason;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
