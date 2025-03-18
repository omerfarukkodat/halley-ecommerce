package com.kodat.of.halleyecommerce.brand;

import com.kodat.of.halleyecommerce.dto.brand.BrandDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;


    @GetMapping
    public ResponseEntity<List<BrandDto>> getAll() {
        return ResponseEntity.ok(brandService.getAll());
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<BrandDto> add(@RequestBody @Valid BrandDto brandDto , Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.add(brandDto,connectedUser));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable Long id , Authentication connectedUser) {
        brandService.delete(id , connectedUser);
        return ResponseEntity.noContent().build();
    }

}
