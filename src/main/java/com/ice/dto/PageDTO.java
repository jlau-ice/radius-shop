package com.ice.dto;

import lombok.Data;

@Data
public class PageDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer status;
    private String keyword;
    private Long categoryId;
}
