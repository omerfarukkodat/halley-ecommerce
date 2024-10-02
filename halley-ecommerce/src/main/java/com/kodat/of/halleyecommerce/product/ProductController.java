package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody @Valid ProductDto productDto,
            Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(productDto , connectedUser));
    }
    @Secured("ADMIN")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductDto productDto,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(productService.updateProduct(productId , productDto , connectedUser));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> findAllProducts(
          @RequestParam(name = "page" , defaultValue = "0" , required = false) int page,
          @RequestParam(name = "size" , defaultValue = "10" , required = false) int size,
          @RequestParam(name = "sort" , defaultValue = "productCode") String sort,
          @RequestParam(name = "order", defaultValue = "desc") String order
    ){
        return ResponseEntity.ok(productService.findAllProducts(page,size , sort , order));
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findProductById(
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(productService.findProductById(productId));
    }
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageResponse<ProductDto>> findProductsByCategoryId(
            @RequestParam(name = "page" , defaultValue = "0" , required = false) int page,
            @RequestParam(name = "size" , defaultValue = "10" , required = false) int size,
            @PathVariable Long categoryId
    ){
        return ResponseEntity.ok(productService.findProductsByCategoryId(page,size,categoryId));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long productId,
            Authentication connectedUser
    ){
        productService.deleteProductById(productId , connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductDto>> findProductsBySearch(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(productService.findProductsBySearch(searchTerm,page,size));
    }




}
