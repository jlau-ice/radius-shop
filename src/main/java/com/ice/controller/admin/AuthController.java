package com.ice.controller.admin;

import com.ice.common.result.Result;
import com.ice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController("adminAuthController")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AuthController {
    private final AdminService adminService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        String token = adminService.login(body.get("username"), body.get("password"));
        return Result.ok(Map.of("token", token));
    }
}
