package com.ice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.exception.BusinessException;
import com.ice.entity.CartItem;
import com.ice.entity.Product;
import com.ice.mapper.CartItemMapper;
import com.ice.mapper.ProductMapper;
import com.ice.vo.CartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService extends ServiceImpl<CartItemMapper, CartItem> {
    private final ProductMapper productMapper;

    public List<CartVO> list(Long userId) {
        return lambdaQuery().eq(CartItem::getUserId, userId).list().stream()
                .map(c -> {
                    Product p = productMapper.selectById(c.getProductId());
                    return CartVO.builder()
                            .id(c.getId()).productId(c.getProductId())
                            .productName(p != null ? p.getName() : "已下架")
                            .coverImage(p != null ? p.getCoverImage() : null)
                            .price(p != null ? p.getPrice() : java.math.BigDecimal.ZERO)
                            .skuInfo(c.getSkuInfo()).quantity(c.getQuantity())
                            .checked(c.getChecked())
                            .stock(p != null ? p.getStock() : 0)
                            .build();
                }).collect(Collectors.toList());
    }

    public void add(Long userId, Long productId, String skuInfo, Integer quantity) {
        // 找同商品同规格的购物车项
        CartItem exist = lambdaQuery()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, productId)
                .eq(skuInfo != null, CartItem::getSkuInfo, skuInfo)
                .one();
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + quantity);
            updateById(exist);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setSkuInfo(skuInfo);
            item.setQuantity(quantity);
            item.setChecked(1);
            save(item);
        }
    }

    public void removeBatch(Long userId, List<Long> cartIds) {
        for (Long id : cartIds) {
            CartItem item = getById(id);
            if (item == null || !item.getUserId().equals(userId)) {
                throw new BusinessException("无权操作");
            }
            removeById(id);
        }
    }
}
