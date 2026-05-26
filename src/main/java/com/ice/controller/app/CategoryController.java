package com.ice.controller.app;

import com.ice.common.result.Result;
import com.ice.service.CategoryService;
import com.ice.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController("appCategoryController")
@RequestMapping("/api/app/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Result<List<CategoryVO>> list() {
        return Result.ok(categoryService.treeList());
    }
}
