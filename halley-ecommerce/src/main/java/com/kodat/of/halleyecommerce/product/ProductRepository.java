package com.kodat.of.halleyecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductCode(String productCode);

    Product findByProductCode(String productCode);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE c.id = :categoryId AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findByCategories_Id(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findAllWithFilters
            (@Param("categoryId") Long categoryId,
             @Param("minPrice") BigDecimal minPrice,
             @Param("maxPrice") BigDecimal maxPrice,
             Pageable pageable,
             @Param("wallpaperType") String wallpaperType,
             @Param("wallpaperSize") String wallpaperSize,
             @Param("brand") Long brand);

    Page<Product> findByCategories_IdInAndIdNot(Set<Long> categoryIds, Long productId, Pageable pageable);

//    @Query(value = "SELECT * FROM products p WHERE p.is_featured = true" +
//            "  ORDER BY p.created_time DESC LIMIT :limit", nativeQuery = true)
//    List<Product> findTopFeaturedProducts(@Param("limit") int limit);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE p.isFeatured = true AND " +
            "(:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) AND " +
            "p.discountedPrice < p.originalPrice AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) " +
            "ORDER BY p.createdTime DESC")
    Page<Product> findTopFeaturedProductsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable
    );





    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) AND " +
            "p.discountedPrice < p.originalPrice AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize)")
    Page<Product> findDiscountedProducts(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE p.id IN :ids AND " +
            "(:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize)")
    Page<Product> findAllByIdsWithPagination(
            @Param("ids") List<Long> productIds,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE p.brand.name ILIKE :brand AND " +
            "(:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize)")
    Page<Product> findByBrandAndFilters(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            @Param("brand") String brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);


    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.categories c " +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) " +
            "ORDER BY p.createdTime DESC")
    Page<Product> findLastAddedWithFilters(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.colours c " +
            "WHERE c.id = :colourId AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findByColours_Id(
            @Param("colourId") Long colourId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.designs d " +
            "WHERE d.id = :designId AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findByDesigns_Id(
            @Param("designId") Long designId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);


    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.rooms r " +
            "WHERE r.id = :roomId AND " +
            "(:brand IS NULL OR p.brand.id = :brand) AND " +
            "(:wallpaperType IS NULL OR p.wallpaperType = :wallpaperType) AND " +
            "(:wallpaperSize IS NULL OR p.size = :wallpaperSize) AND " +
            "(:minPrice IS NULL OR p.discountedPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.discountedPrice <= :maxPrice)")
    Page<Product> findByRooms_Id(
            @Param("roomId") Long roomId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") Long brand,
            @Param("wallpaperType") String wallpaperType,
            @Param("wallpaperSize") String wallpaperSize,
            Pageable pageable);
}