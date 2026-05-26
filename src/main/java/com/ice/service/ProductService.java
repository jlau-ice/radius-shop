package com.ice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.dto.ProductDTO;
import com.ice.entity.Product;
import com.ice.entity.ProductSku;
import com.ice.mapper.ProductMapper;
import com.ice.mapper.ProductSkuMapper;
import com.ice.vo.ProductVO;
import com.ice.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import cn.hutool.json.JSONUtil;

@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {
    private final ProductSkuMapper skuMapper;
    private final CategoryService categoryService;

    public ProductVO toVO(Product p) {
        LambdaQueryWrapper<ProductSku> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductSku::getProductId, p.getId());
        List<ProductSku> skus = skuMapper.selectList(qw);

        return ProductVO.builder()
                .id(p.getId()).categoryId(p.getCategoryId())
                .categoryName(categoryService.getById(p.getCategoryId()).getName())
                .name(p.getName()).description(p.getDescription())
                .coverImage(p.getCoverImage())
                .images(p.getImages() != null ? JSONUtil.parseArray(p.getImages()).toList(String.class) : null)
                .price(p.getPrice()).originPrice(p.getOriginPrice())
                .unit(p.getUnit()).stock(p.getStock()).sales(p.getSales())
                .status(p.getStatus()).sortOrder(p.getSortOrder()).isRecommend(p.getIsRecommend())
                .skuList(skus.stream().map(s -> SkuVO.builder()
                        .id(s.getId()).specName(s.getSpecName()).specValue(s.getSpecValue())
                        .priceDiff(s.getPriceDiff()).stock(s.getStock()).build()).collect(Collectors.toList()))
                .createTime(p.getCreateTime())
                .build();
    }

    @Transactional
    public void saveProduct(ProductDTO dto) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setCategoryId(dto.getCategoryId());
        p.setDescription(dto.getDescription());
        p.setCoverImage(dto.getCoverImage());
        p.setImages(dto.getImages() != null ? JSONUtil.toJsonStr(dto.getImages()) : null);
        p.setPrice(dto.getPrice());
        p.setOriginPrice(dto.getOriginPrice());
        p.setUnit(dto.getUnit() != null ? dto.getUnit() : "份");
        p.setStock(dto.getStock());
        p.setMinStock(dto.getMinStock() != null ? dto.getMinStock() : 0);
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        p.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        p.setIsRecommend(dto.getIsRecommend() != null ? dto.getIsRecommend() : 0);
        save(p);

        if (dto.getSkuList() != null) {
            for (var s : dto.getSkuList()) {
                ProductSku sku = new ProductSku();
                sku.setProductId(p.getId());
                sku.setSpecName(s.getSpecName());
                sku.setSpecValue(s.getSpecValue());
                sku.setPriceDiff(s.getPriceDiff() != null ? s.getPriceDiff() : java.math.BigDecimal.ZERO);
                sku.setStock(s.getStock() != null ? s.getStock() : 0);
                skuMapper.insert(sku);
            }
        }
    }

    @Transactional
    public void updateProduct(ProductDTO dto) {
        Product p = getById(dto.getId());
        p.setName(dto.getName());
        p.setCategoryId(dto.getCategoryId());
        p.setDescription(dto.getDescription());
        p.setCoverImage(dto.getCoverImage());
        p.setImages(dto.getImages() != null ? JSONUtil.toJsonStr(dto.getImages()) : null);
        p.setPrice(dto.getPrice());
        p.setOriginPrice(dto.getOriginPrice());
        p.setUnit(dto.getUnit() != null ? dto.getUnit() : "份");
        p.setStock(dto.getStock());
        p.setMinStock(dto.getMinStock() != null ? dto.getMinStock() : 0);
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        p.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        p.setIsRecommend(dto.getIsRecommend() != null ? dto.getIsRecommend() : 0);
        updateById(p);

        // 删旧规格
        LambdaQueryWrapper<ProductSku> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductSku::getProductId, dto.getId());
        skuMapper.delete(qw);

        if (dto.getSkuList() != null) {
            for (var s : dto.getSkuList()) {
                ProductSku sku = new ProductSku();
                sku.setProductId(p.getId());
                sku.setSpecName(s.getSpecName());
                sku.setSpecValue(s.getSpecValue());
                sku.setPriceDiff(s.getPriceDiff() != null ? s.getPriceDiff() : java.math.BigDecimal.ZERO);
                sku.setStock(s.getStock() != null ? s.getStock() : 0);
                skuMapper.insert(sku);
            }
        }
    }

    @Transactional
    public void deductStock(Long productId, int quantity) {
        Product p = getById(productId);
        if (p.getStock() < quantity) {
            throw new com.ice.common.exception.BusinessException(p.getName() + " 库存不足");
        }
        p.setStock(p.getStock() - quantity);
        p.setSales((p.getSales() != null ? p.getSales() : 0) + quantity);
        updateById(p);
    }
}
