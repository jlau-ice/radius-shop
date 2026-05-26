package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.dto.LoginDTO;
import com.ice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.wxLogin(dto.getCode(), dto.getNickname(), dto.getAvatarUrl()));
    }

    @GetMapping("/info")
    public Result<?> info() {
        return Result.ok(userService.toVO(userService.currentUser()));
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        cn.dev33.satoken.stp.StpUtil.logout();
        return Result.ok();
    }
}
