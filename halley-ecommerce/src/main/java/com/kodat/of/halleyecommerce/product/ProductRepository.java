package com.kodat.of.halleyecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);
}

