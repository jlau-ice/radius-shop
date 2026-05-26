package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.dto.OrderSubmitDTO;
import com.ice.entity.Order;
import com.ice.service.OrderService;
import com.ice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("appOrderController")
@RequestMapping("/api/app/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody OrderSubmitDTO dto) {
        Order order = orderService.submit(userService.currentUserId(), dto.getAddressId(), dto.getRemark());
        return Result.ok(order);
    }

    @GetMapping("/pay/{orderId}")
    public Result<?> pay(@PathVariable Long orderId) {
        return Result.ok(orderService.createPayment(orderId, userService.currentUserId()));
    }

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String status) {
        return Result.ok(orderService.pageByUser(userService.currentUserId(), page, pageSize, status));
    }

    @PostMapping("/{id}/mock-pay")
    public Result<?> mockPay(@PathVariable Long id) {
        orderService.mockPay(userService.currentUserId(), id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(orderService.detailByUser(userService.currentUserId(), id));
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        orderService.cancel(userService.currentUserId(), id);
        return Result.ok();
    }
}
