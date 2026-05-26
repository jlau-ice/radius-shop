package com.ice.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StatsVO {
    private Long totalOrders;
    private BigDecimal totalAmount;
    private Long totalUsers;
    private Long totalProducts;
    private List<StatsItemVO> todayStats;
}
