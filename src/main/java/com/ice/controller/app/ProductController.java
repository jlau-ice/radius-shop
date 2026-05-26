package com.ice.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.common.result.Result;
import com.ice.entity.Product;
import com.ice.service.ProductService;
import com.ice.vo.PageVO;
import com.ice.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController("appProductController")
@RequestMapping("/api/app/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/page")
    public Result<PageVO<ProductVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer isRecommend,
            @RequestParam(required = false) String keyword) {
        IPage<Product> pg = productService.lambdaQuery()
                .eq(Product::getStatus, 1)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .eq(isRecommend != null, Product::getIsRecommend, isRecommend)
                .like(keyword != null && !keyword.isBlank(), Product::getName, keyword)
                .orderByDesc(Product::getSortOrder)
                .page(new Page<>(page, pageSize));
        return Result.ok(PageVO.<ProductVO>builder()
                .total(pg.getTotal()).pages(pg.getPages()).current(page)
                .records(pg.getRecords().stream().map(productService::toVO).collect(Collectors.toList()))
                .build());
    }

    @GetMapping("/{id}")
    public Result<ProductVO> detail(@PathVariable Long id) {
        Product p = productService.getById(id);
        if (p == null || p.getStatus() == 0) {
            return Result.fail("商品已下架");
        }
        return Result.ok(productService.toVO(p));
    }
}
