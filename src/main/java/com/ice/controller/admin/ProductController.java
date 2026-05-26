package com.ice.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.common.result.Result;
import com.ice.dto.ProductDTO;
import com.ice.entity.Product;
import com.ice.service.ProductService;
import com.ice.service.impl.CosService;
import com.ice.vo.PageVO;
import com.ice.vo.ProductVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;

@RestController("adminProductController")
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CosService cosService;

    @GetMapping("/page")
    public Result<PageVO<ProductVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        IPage<Product> pg = productService.lambdaQuery()
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .like(keyword != null, Product::getName, keyword)
                .orderByDesc(Product::getCreateTime)
                .page(new Page<>(page, pageSize));
        return Result.ok(PageVO.<ProductVO>builder()
                .total(pg.getTotal()).pages(pg.getPages()).current(page)
                .records(pg.getRecords().stream().map(productService::toVO).collect(Collectors.toList()))
                .build());
    }

    @GetMapping("/{id}")
    public Result<ProductVO> detail(@PathVariable Long id) {
        return Result.ok(productService.toVO(productService.getById(id)));
    }

    @PostMapping
    public Result<?> save(@Valid @RequestBody ProductDTO dto) {
        productService.saveProduct(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        dto.setId(id);
        productService.updateProduct(dto);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<?> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        Product p = productService.getById(id);
        p.setStatus(status);
        productService.updateById(p);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.ok();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        return Result.ok(cosService.upload(file));
    }
}
