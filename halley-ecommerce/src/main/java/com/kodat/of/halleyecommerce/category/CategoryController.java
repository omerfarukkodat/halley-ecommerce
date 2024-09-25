package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kategoriler")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }
    @Secured("ADMIN")
    @PostMapping("/addMainCategory")
    public ResponseEntity<CategoryDto> addParentCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.addParentCategory(categoryDto , connectedAdmin));
    }
    @Secured("ADMIN")
    @PostMapping("/{parentCategory}/addChildCategory")
    public ResponseEntity<CategoryDto> addChildCategory(
            @PathVariable("parentCategory") String parentCategoryName,
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin
    ){
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.addChildCategory(parentCategoryName,categoryDto,connectedAdmin));
    }

}
