package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findAllWithFilters(Set<Long> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByCategories_IdInAndIdNot(Set<Long> categoryIds, Long productId, Pageable pageable);

    @Query(value = "SELECT * FROM products p WHERE p.is_featured = true  ORDER BY p.created_time DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopFeaturedProducts(@Param("limit") int limit);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE (:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
            "(:minPrice IS NULL OR p.originalPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.originalPrice <= :maxPrice) AND " +
            "p.discountedPrice < p.originalPrice")
    Page<Product> findDiscountedProducts(Set<Long> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}