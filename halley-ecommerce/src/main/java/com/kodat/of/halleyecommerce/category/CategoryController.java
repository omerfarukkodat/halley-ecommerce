package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kategoriler")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ana-kategori-ekle")
    public ResponseEntity<CategoryDto> addParentCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin) {
        return new ResponseEntity<>(service.addParentCategory(categoryDto , connectedAdmin), HttpStatus.CREATED);
    }

}
