package com.kodat.of.halleyecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Applying fuzzy search using trigram
    // Basit LIKE ile Fuzzy Search
    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :searchTerm, '%'))")
    Page<Product> fuzzySearchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Applying fuzzy search with Levenshtein distance
    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE %:searchTerm%")
    Page<Product> fuzzySearchByNameWithWildcard(@Param("searchTerm") String searchTerm, Pageable pageable);

}

