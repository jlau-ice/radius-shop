package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderVO {
    private Long id;
    private String orderNo;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private String deliveryAddress;
    private String deliveryContact;
    private String deliveryPhone;
    private String deliveryPhoto;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime createTime;
    private List<OrderItemVO> items;
    private DeliveryInfoVO deliveryInfo;
}
