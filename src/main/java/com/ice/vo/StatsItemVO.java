package com.ice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsItemVO {
    private String label;
    private Object value;
}
