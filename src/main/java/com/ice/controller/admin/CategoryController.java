package com.ice.controller.admin;

import com.ice.common.result.Result;
import com.ice.dto.CategoryDTO;
import com.ice.entity.Category;
import com.ice.service.CategoryService;
import com.ice.service.impl.CosService;
import com.ice.vo.CategoryVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CosService cosService;

    @GetMapping("/tree")
    public Result<List<CategoryVO>> tree() {
        return Result.ok(categoryService.treeList());
    }

    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.ok(categoryService.list());
    }

    @PostMapping
    public Result<?> save(@Valid @RequestBody CategoryDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setIcon(dto.getIcon());
        c.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        c.setParentId(dto.getParentId() != null ? dto.getParentId() : 0);
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        categoryService.save(c);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        Category c = categoryService.getById(id);
        c.setName(dto.getName());
        c.setIcon(dto.getIcon());
        c.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        c.setParentId(dto.getParentId() != null ? dto.getParentId() : 0);
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        categoryService.updateById(c);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.ok();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        return Result.ok(cosService.upload(file));
    }
}
