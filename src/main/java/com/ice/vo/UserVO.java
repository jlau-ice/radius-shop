package com.ice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    private Long id;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private AddressVO defaultAddress;
}
