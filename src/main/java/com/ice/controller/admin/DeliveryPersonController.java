package com.ice.controller.admin;

import com.ice.common.result.Result;
import com.ice.entity.DeliveryPerson;
import com.ice.service.DeliveryService;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/delivery")
@RequiredArgsConstructor
public class DeliveryPersonController {
    private final DeliveryService deliveryService;

    @GetMapping("/list")
    public Result<List<DeliveryPerson>> list() {
        return Result.ok(deliveryService.list());
    }

    @PostMapping
    public Result<?> save(@RequestBody DeliveryPerson dp) {
        dp.setPassword(BCrypt.hashpw(dp.getPassword()));
        dp.setStatus(1);
        deliveryService.save(dp);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody DeliveryPerson dp) {
        DeliveryPerson exist = deliveryService.getById(id);
        exist.setName(dp.getName());
        exist.setPhone(dp.getPhone());
        exist.setStatus(dp.getStatus());
        if (dp.getPassword() != null && !dp.getPassword().isEmpty()) {
            exist.setPassword(BCrypt.hashpw(dp.getPassword()));
        }
        deliveryService.updateById(exist);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deliveryService.removeById(id);
        return Result.ok();
    }
}
