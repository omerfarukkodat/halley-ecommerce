package com.kodat.of.halleyecommerce.product;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productDto , connectedUser));
    }
    @Secured("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductDto productDto,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(productService.updateProduct(id , productDto , connectedUser));
    }





}
