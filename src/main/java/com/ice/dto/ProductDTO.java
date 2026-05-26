package com.ice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    @NotBlank(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    private String description;
    private String coverImage;
    private List<String> images;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    private BigDecimal originPrice;
    private String unit;
    @NotNull(message = "库存不能为空")
    private Integer stock;
    private Integer minStock;
    private Integer status;
    private Integer sortOrder;
    private Integer isRecommend;
    private List<SkuDTO> skuList;
}
