package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryTreeDto;
import com.kodat.of.halleyecommerce.dto.category.MainCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @Operation(summary = "Create main category", description = "Creating parent category ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Main category successfully created"),
            @ApiResponse(responseCode = "409", description = "Category already exists"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access attempt by non-admin user")
    })
    @Secured("ADMIN")
    @PostMapping("/addMainCategory")
    public ResponseEntity<CategoryDto> addParentCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addParentCategory(categoryDto, connectedAdmin));
    }

    @Operation(summary = "Create child category", description = "Creating child category ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Child category successfully created"),
            @ApiResponse(responseCode = "409", description = "Category already exists"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access attempt by non-admin user")
    })
    @Secured("ADMIN")
    @PostMapping("/{parentCategory}/addChildCategory")
    public ResponseEntity<CategoryDto> addChildCategory(
            @PathVariable("parentCategory") Long parentCategoryId,
            @RequestBody @Valid CategoryDto categoryDto,
            Authentication connectedAdmin
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addChildCategory(parentCategoryId, categoryDto, connectedAdmin));
    }

    @Operation(summary = "Get all categories", description = "Getting all categories ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Categories successfully fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/findAll")
    public ResponseEntity<PageResponse<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.getAllCategories(page, size));
    }

    @Operation(summary = "Get all category trees", description = "Getting all category trees ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Categories tree's successfully fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeDto>> getCategoryTree() {
        return ResponseEntity.ok(service.getCategoryTree());
    }

    @Operation(summary = "Update category", description = "Update category with specific category id's ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated category"),
            @ApiResponse(responseCode = "404", description = "Category does not exists"),
            @ApiResponse(responseCode = "409", description = "Category already exists")
    })

    @GetMapping("/getMainCategories")
    public ResponseEntity<List<MainCategoryDto>> getMainCategories() {
        return ResponseEntity.ok(service.getMainCategories());
    }


    @Secured("ADMIN")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryDto categoryDto,
            Authentication connectedAdmin
    ) {
        return ResponseEntity.ok(service.updateCategory(categoryId, categoryDto, connectedAdmin));
    }

    @Operation(summary = "Delete category", description = "Delete category with specific category id's ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted category"),
            @ApiResponse(responseCode = "404", description = "Category does not exists"),
    })
    @Secured("ADMIN")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId, Authentication connectedAdmin) {
        service.deleteCategory(categoryId, connectedAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categorySlug}")
    public ResponseEntity<CategoryTreeDto> getCategory(@PathVariable String categorySlug) {
        return ResponseEntity.ok(service.getCategory(categorySlug));
    }

    @GetMapping("/category-paths/{categorySlug}")
    public ResponseEntity<List<CategoryPathDto>> getCategoryPaths(@PathVariable String categorySlug) {
        return ResponseEntity.ok(service.getCategoryPaths(categorySlug));
    }






}
