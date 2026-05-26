package com.ice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressVO {
    private Long id;
    private String name;
    private String address;
    private String contactName;
    private String contactPhone;
}
