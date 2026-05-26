package com.ice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderSubmitDTO {
    @NotNull(message = "地址不能为空")
    private Long addressId;
    private String remark;
}
