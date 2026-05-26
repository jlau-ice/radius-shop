package com.ice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
    private Integer status;
}
