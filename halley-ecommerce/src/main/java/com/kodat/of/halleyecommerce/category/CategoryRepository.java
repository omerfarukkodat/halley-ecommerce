package com.kodat.of.halleyecommerce.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCategoryName(String categoryName);

    List<Category> findByParentIsNull();

     Optional<Category> findCategoryBySlug(String slug);


    List<Category> findByParent_Id(Long categoryId);
}
