package com.ice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SkuDTO {
    private String specName;
    private String specValue;
    private BigDecimal priceDiff;
    private Integer stock;
}
