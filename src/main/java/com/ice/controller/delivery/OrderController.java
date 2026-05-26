package com.ice.controller.delivery;

import com.ice.common.result.Result;
import com.ice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("deliveryOrderController")
@RequestMapping("/api/delivery/order")
@RequiredArgsConstructor
public class OrderController {
    private final DeliveryService deliveryService;

    @GetMapping("/pending")
    public Result<?> pending(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(deliveryService.pendingOrders(page, pageSize));
    }

    @GetMapping("/delivering")
    public Result<?> delivering(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(deliveryService.myDeliveringOrders(page, pageSize));
    }

    @GetMapping("/done")
    public Result<?> done(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(deliveryService.myDoneOrders(page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(deliveryService.detail(id));
    }

    @PostMapping("/{id}/accept")
    public Result<?> accept(@PathVariable Long id) {
        deliveryService.accept(id);
        return Result.ok();
    }

    @PostMapping("/{id}/complete")
    public Result<?> complete(@PathVariable Long id, @RequestParam MultipartFile photo) {
        deliveryService.complete(id, photo);
        return Result.ok();
    }
}
