package com.ice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "地址不能为空")
    private String address;
    private String contactName;
    private String contactPhone;
    private Integer sortOrder;
    private Integer status;
}
