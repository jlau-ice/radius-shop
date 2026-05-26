package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private String coverImage;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal originPrice;
    private String unit;
    private Integer stock;
    private Integer sales;
    private Integer status;
    private Integer sortOrder;
    private Integer isRecommend;
    private List<SkuVO> skuList;
    private LocalDateTime createTime;
}
