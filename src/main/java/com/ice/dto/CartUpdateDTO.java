package com.ice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartUpdateDTO {
    @NotNull(message = "购物车项ID不能为空")
    private Long id;
    private Integer quantity;
    private Integer checked;
}
