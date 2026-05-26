package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PageVO<T> {
    private Long total;
    private Long pages;
    private Integer current;
    private List<T> records;
}
