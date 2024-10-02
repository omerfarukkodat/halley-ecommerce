package com.kodat.of.halleyecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);

    Page<Product> findByCategories_Id(Long categoryId, Pageable pageable);

    // Levenshtein Search
//    @Query(value = "SELECT p.*, (1.0 / (1 + levenshtein(p.name, :searchTerm))) AS relevance " +
//            "FROM products p " +
//            "WHERE levenshtein(p.name, :searchTerm) < 20 " +
//            "ORDER BY relevance DESC",
//            countQuery = "SELECT COUNT(*) " +
//                    "FROM products p " +
//                    "WHERE levenshtein(p.name, :searchTerm) < 20",
//            nativeQuery = true)
//    Page<Product> findProductsByLevenshteinSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    //Full-text Search
    @Query(value = "SELECT p.*, " +
            "ts_rank_cd(to_tsvector('turkish', p.name), plainto_tsquery('turkish', :searchTerm)) AS relevance " +
            "FROM products p " +
            "WHERE to_tsvector('turkish', p.name) @@ plainto_tsquery('turkish', :searchTerm) " +
            "OR p.name ILIKE CONCAT('%', :searchTerm, '%') " +
            "ORDER BY relevance DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM products p " +
                    "WHERE to_tsvector('turkish', p.name) @@ plainto_tsquery('turkish', :searchTerm) " +
                    "OR p.name ILIKE CONCAT('%', :searchTerm, '%')",
            nativeQuery = true)
    Page<Product> findProductsByFullTextSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
    // Trigram Search
//    @Query(value = "SELECT p.*, similarity(p.name, :searchTerm) AS relevance " +
//            "FROM products p " +
//            "WHERE similarity(p.name, :searchTerm) > 0.2 " +
//            "ORDER BY relevance DESC",
//            countQuery = "SELECT COUNT(*) " +
//                    "FROM products p " +
//                    "WHERE similarity(p.name, :searchTerm) > 0.2",
//            nativeQuery = true)
//    Page<Product> findProductsByTrigramSearch(@Param("searchTerm") String searchTerm, Pageable pageable);



}

