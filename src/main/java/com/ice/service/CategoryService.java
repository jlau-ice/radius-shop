package com.ice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.entity.Category;
import com.ice.mapper.CategoryMapper;
import com.ice.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public List<CategoryVO> treeList() {
        List<Category> all = lambdaQuery().eq(Category::getStatus, 1).orderByAsc(Category::getSortOrder).list();
        List<Category> roots = all.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        return roots.stream().map(r -> buildTree(r, all)).collect(Collectors.toList());
    }

    private CategoryVO buildTree(Category c, List<Category> all) {
        List<CategoryVO> children = all.stream()
                .filter(ch -> ch.getParentId().equals(c.getId()))
                .map(ch -> buildTree(ch, all))
                .collect(Collectors.toList());
        return CategoryVO.builder()
                .id(c.getId()).name(c.getName()).icon(c.getIcon())
                .sortOrder(c.getSortOrder()).parentId(c.getParentId()).status(c.getStatus())
                .children(children.isEmpty() ? null : children)
                .build();
    }
}
