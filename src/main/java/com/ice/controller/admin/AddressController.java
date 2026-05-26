package com.ice.controller.admin;

import com.ice.common.result.Result;
import com.ice.dto.AddressDTO;
import com.ice.entity.AddressTemplate;
import com.ice.service.AddressService;
import com.ice.vo.AddressVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController("adminAddressController")
@RequestMapping("/api/admin/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/list")
    public Result<List<AddressVO>> list() {
        return Result.ok(addressService.list().stream().map(addressService::toVO).collect(Collectors.toList()));
    }

    @PostMapping
    public Result<?> save(@Valid @RequestBody AddressDTO dto) {
        AddressTemplate a = new AddressTemplate();
        a.setName(dto.getName());
        a.setAddress(dto.getAddress());
        a.setContactName(dto.getContactName());
        a.setContactPhone(dto.getContactPhone());
        a.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        a.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        addressService.save(a);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody AddressDTO dto) {
        AddressTemplate a = addressService.getById(id);
        a.setName(dto.getName());
        a.setAddress(dto.getAddress());
        a.setContactName(dto.getContactName());
        a.setContactPhone(dto.getContactPhone());
        a.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        a.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        addressService.updateById(a);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.ok();
    }
}
