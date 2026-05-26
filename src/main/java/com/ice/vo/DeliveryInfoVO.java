package com.ice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryInfoVO {
    private Long personId;
    private String personName;
    private String personPhone;
}
