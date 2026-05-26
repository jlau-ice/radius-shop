package com.ice.controller.delivery;

import com.ice.common.result.Result;
import com.ice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController("deliveryAuthController")
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class AuthController {
    private final DeliveryService deliveryService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        String token = deliveryService.login(body.get("username"), body.get("password"));
        return Result.ok(Map.of("token", token));
    }
}
