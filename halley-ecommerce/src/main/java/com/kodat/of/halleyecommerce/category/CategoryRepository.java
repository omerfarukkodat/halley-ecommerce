package com.kodat.of.halleyecommerce.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String parentCategory);

}
