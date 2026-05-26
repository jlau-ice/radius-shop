package com.ice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartAddDTO {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    private String skuInfo;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
