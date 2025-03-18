package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<ProductDto> add(@RequestBody @Valid ProductDto productDto, Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(productDto, connectedUser));
    }

    @Secured("ADMIN")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable Long productId, @RequestBody @Valid ProductDto productDto,
                                             Authentication connectedUser) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDto, connectedUser));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId, Authentication connectedUser) {
        productService.deleteProductById(productId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}-{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable String slug, @PathVariable Long productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> findAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {
        return ResponseEntity.ok(productService
                .findAllProducts(page, size, sortBy, sortDirection, minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<PageResponse<ProductDto>> findProductsByCategoryId(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode", required = false) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
            @PathVariable String slug,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {
        return ResponseEntity.ok(productService
                .findProductsByCategoryId(page, size, slug, sortBy, sortDirection, minPrice, maxPrice, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductDto>> findProductsBySearch(
            @RequestParam String searchTerm,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {

        return ResponseEntity.ok(productService.findProductsBySearch(
                searchTerm, page, size, sortBy, sortDirection, minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/filter")
    public ResponseEntity<PageResponse<ProductDto>> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {
        return ResponseEntity.ok(productService
                .filterProducts(categoryId, minPrice, maxPrice, page, size, sortBy, sortDirection, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/similar/{productId}")
    public ResponseEntity<PageResponse<ProductDto>> findSimilarProducts(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.findSimilarProducts(productId, page, size));
    }

    @GetMapping("/featured")
    public ResponseEntity<PageResponse<ProductDto>> getFeaturedProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize

    ) {
        return ResponseEntity.ok(productService.findFeaturedProducts(
                        categoryId,minPrice,maxPrice,page,size,sortBy,sortDirection,brand,wallpaperType,wallpaperSize));
    }


    @GetMapping("/discounted")
    public ResponseEntity<PageResponse<ProductDto>> getDiscountedProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize) {
        return ResponseEntity.ok(productService
                .getDiscountedProducts(categoryId, minPrice, maxPrice, page, size, sortBy, sortDirection, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/{productId}/stock")
    public ResponseEntity<Integer> getStockById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getStockById(productId));
    }

    @GetMapping("/{productId}/category-paths")
    public ResponseEntity<List<CategoryPathDto>> getCategoryPaths(@PathVariable Long productId, @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(productService.getCategoryPaths(productId, categoryId));
    }


    @GetMapping("/brands/{brand}")
    public ResponseEntity<PageResponse<ProductDto>> findAllByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {
        return ResponseEntity.ok(productService
                .findAllByBrand(brand, page, size, sortBy, sortDirection, minPrice, maxPrice, categoryId, wallpaperType, wallpaperSize));
    }

    @GetMapping("/lastAdded")
    public ResponseEntity<PageResponse<ProductDto>> findLastAdded(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize
    ) {
        return ResponseEntity.ok(productService
                .findLastAdded(page, size, sortBy, sortDirection, minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize));
    }

    @GetMapping("/colour/{slug}")
    public ResponseEntity<PageResponse<ProductDto>> findByColourSlug(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode", required = false) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice
    ) {
        return
                ResponseEntity.ok(productService.findByColourSlug(
                        slug,
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        brand,
                        wallpaperType,
                        wallpaperSize,
                        maxPrice,
                        minPrice));
    }

    @GetMapping("/design/{slug}")
    public ResponseEntity<PageResponse<ProductDto>> findByDesignSlug(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode", required = false) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice
    ) {
        return
                ResponseEntity.ok(productService.findByDesignSlug(
                        slug,
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        brand,
                        wallpaperType,
                        wallpaperSize,
                        maxPrice,
                        minPrice));
    }

    @GetMapping("/room/{slug}")
    public ResponseEntity<PageResponse<ProductDto>> findByRoomSlug(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "productCode", required = false) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) String wallpaperType,
            @RequestParam(required = false) String wallpaperSize,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice
    ) {
        return
                ResponseEntity.ok(productService.findByRoomSlug(
                        slug,
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        brand,
                        wallpaperType,
                        wallpaperSize,
                        maxPrice,
                        minPrice));
    }


}
