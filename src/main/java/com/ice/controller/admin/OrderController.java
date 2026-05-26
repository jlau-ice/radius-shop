package com.ice.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.common.constant.OrderStatus;
import com.ice.common.exception.BusinessException;
import com.ice.common.result.Result;
import com.ice.entity.Order;
import com.ice.service.OrderService;
import com.ice.vo.OrderVO;
import com.ice.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/page")
    public Result<PageVO<OrderVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status) {
        if (status != null && !status.isBlank() && !OrderStatus.isValid(status)) {
            throw new BusinessException("订单状态不正确");
        }
        IPage<Order> pg = orderService.lambdaQuery()
                .eq(status != null && !status.isEmpty(), Order::getStatus, status)
                .orderByDesc(Order::getCreateTime)
                .page(new Page<>(page, pageSize));
        return Result.ok(PageVO.<OrderVO>builder()
                .total(pg.getTotal()).pages(pg.getPages()).current(page)
                .records(pg.getRecords().stream().map(orderService::toVO).collect(Collectors.toList()))
                .build());
    }

    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        return Result.ok(orderService.detailForAdmin(id));
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!OrderStatus.PREPARING.equals(status)) {
            throw new BusinessException("后台只能将已支付订单改为制作中");
        }
        orderService.startPreparing(id);
        return Result.ok();
    }
}
