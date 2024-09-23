package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



}
