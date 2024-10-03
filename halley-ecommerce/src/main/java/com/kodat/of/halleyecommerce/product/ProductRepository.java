package com.kodat.of.halleyecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Set;


public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);

    Page<Product> findByCategories_Id(Long categoryId, Pageable pageable);


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


    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE (:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findAllWithFilters(Set<Long> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);




}





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



