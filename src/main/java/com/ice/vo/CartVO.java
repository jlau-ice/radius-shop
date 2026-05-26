package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CartVO {
    private Long id;
    private Long productId;
    private String productName;
    private String coverImage;
    private BigDecimal price;
    private String skuInfo;
    private Integer quantity;
    private Integer checked;
    private Integer stock;
}
