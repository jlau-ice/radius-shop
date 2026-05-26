package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SkuVO {
    private Long id;
    private String specName;
    private String specValue;
    private BigDecimal priceDiff;
    private Integer stock;
}
