package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.dto.CartAddDTO;
import com.ice.dto.CartUpdateDTO;
import com.ice.entity.CartItem;
import com.ice.service.CartService;
import com.ice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/app/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public Result<?> list() {
        return Result.ok(cartService.list(userService.currentUserId()));
    }

    @PostMapping
    public Result<?> add(@Valid @RequestBody CartAddDTO dto) {
        cartService.add(userService.currentUserId(), dto.getProductId(), dto.getSkuInfo(), dto.getQuantity());
        return Result.ok();
    }

    @PutMapping
    public Result<?> update(@Valid @RequestBody CartUpdateDTO dto) {
        CartItem item = cartService.getById(dto.getId());
        if (item != null && item.getUserId().equals(userService.currentUserId())) {
            if (dto.getQuantity() != null) item.setQuantity(dto.getQuantity());
            if (dto.getChecked() != null) item.setChecked(dto.getChecked());
            cartService.updateById(item);
        }
        return Result.ok();
    }

    @DeleteMapping("/{ids}")
    public Result<?> remove(@PathVariable String ids) {
        String[] arr = ids.split(",");
        cartService.removeBatch(userService.currentUserId(),
                java.util.Arrays.stream(arr).map(Long::parseLong).toList());
        return Result.ok();
    }
}
