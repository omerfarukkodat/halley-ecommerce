package com.kodat.of.halleyecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}

