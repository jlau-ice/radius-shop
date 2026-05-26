package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CategoryVO {
    private Long id;
    private String name;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
    private Integer status;
    private List<CategoryVO> children;
}
