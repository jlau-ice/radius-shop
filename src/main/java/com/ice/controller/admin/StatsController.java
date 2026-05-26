package com.ice.controller.admin;

import com.ice.common.result.Result;
import com.ice.entity.*;
import com.ice.mapper.*;
import com.ice.vo.StatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsController {
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @GetMapping
    public Result<Map<String, Object>> stats() {
        Long orderCount = orderMapper.selectCount(null);
        Long productCount = productMapper.selectCount(null);
        Long userCount = userMapper.selectCount(null);
        return Result.ok(Map.of(
                "orderCount", orderCount,
                "productCount", productCount,
                "userCount", userCount
        ));
    }
}
