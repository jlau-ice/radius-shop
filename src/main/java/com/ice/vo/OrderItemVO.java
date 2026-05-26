package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemVO {
    private Long productId;
    private String productName;
    private String productImage;
    private String skuInfo;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
}
