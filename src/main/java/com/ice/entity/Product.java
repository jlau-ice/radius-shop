package com.ice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String coverImage;
    private String images;
    private BigDecimal price;
    private BigDecimal originPrice;
    private String unit;
    private Integer stock;
    private Integer minStock;
    private Integer sales;
    private Integer status;
    private Integer sortOrder;
    private Integer isRecommend;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
