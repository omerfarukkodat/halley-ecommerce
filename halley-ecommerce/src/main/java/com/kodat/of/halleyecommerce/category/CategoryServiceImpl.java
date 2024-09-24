package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.mapper.category.CategoryMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryDto addParentCategory(CategoryDto categoryDto , Authentication connectedAdmin) {
       verifyAdminRole(connectedAdmin); // Check admin role
        validateCategoryName(categoryDto.getCategoryName()); // Check categoryName added before?
        Category category = CategoryMapper.toCategory(categoryDto,null);
       return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    private void verifyAdminRole(Authentication connectedAdmin) {
        if (connectedAdmin.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ADMIN"))){
            throw new SecurityException("You are not an admin");
        }
    }

    private void validateCategoryName(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName)){
            throw new RuntimeException("Category: "+ categoryName + " already exists");
        }
    }


}
