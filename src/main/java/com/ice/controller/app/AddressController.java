package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.service.AddressService;
import com.ice.vo.AddressVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController("appAddressController")
@RequestMapping("/api/app/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public Result<List<AddressVO>> list() {
        return Result.ok(addressService.activeList());
    }
}
