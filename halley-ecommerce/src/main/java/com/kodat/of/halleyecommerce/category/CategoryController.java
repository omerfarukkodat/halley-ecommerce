package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.user.enums.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ADMIN")
    @PostMapping("/addMainCategory")
    public ResponseEntity<CategoryDto> addParentCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin) {
        return new ResponseEntity<>(service.addParentCategory(categoryDto , connectedAdmin), HttpStatus.CREATED);
    }
    @Secured("ADMIN")
    @PostMapping("/{parentCategory}/addChildCategory")
    public ResponseEntity<CategoryDto> addChildCategory(
            @PathVariable("parentCategory") String parentCategoryName,
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin
    ){
        return new ResponseEntity<>(service.addChildCategory(parentCategoryName,categoryDto,connectedAdmin),HttpStatus.CREATED);
    }

}
