package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryTreeDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CategoryService {
    CategoryDto addParentCategory(CategoryDto categoryDto, Authentication connectedAdmin);

    CategoryDto addChildCategory(Long parentCategoryId, CategoryDto categoryDto, Authentication connectedAdmin);

    PageResponse<CategoryDto> getAllCategories(int page,int size);

    List<CategoryTreeDto> getCategoryTree();
}
