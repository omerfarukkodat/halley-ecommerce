package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import org.springframework.security.core.Authentication;

public interface CategoryService {
    CategoryDto addParentCategory(CategoryDto categoryDto, Authentication connectedAdmin);

    CategoryDto addChildCategory(String parentCategoryName, CategoryDto categoryDto, Authentication connectedAdmin);
}
