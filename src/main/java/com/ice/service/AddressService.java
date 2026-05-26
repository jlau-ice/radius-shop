package com.ice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.exception.BusinessException;
import com.ice.entity.AddressTemplate;
import com.ice.mapper.AddressTemplateMapper;
import com.ice.vo.AddressVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService extends ServiceImpl<AddressTemplateMapper, AddressTemplate> {

    public List<AddressVO> activeList() {
        return lambdaQuery().eq(AddressTemplate::getStatus, 1)
                .orderByAsc(AddressTemplate::getSortOrder).list()
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    public AddressVO toVO(AddressTemplate a) {
        return AddressVO.builder()
                .id(a.getId()).name(a.getName()).address(a.getAddress())
                .contactName(a.getContactName()).contactPhone(a.getContactPhone())
                .build();
    }

    public AddressTemplate getActiveById(Long id) {
        AddressTemplate a = getById(id);
        if (a == null || a.getStatus() == 0) {
            throw new BusinessException("该配送地址已失效");
        }
        return a;
    }
}
