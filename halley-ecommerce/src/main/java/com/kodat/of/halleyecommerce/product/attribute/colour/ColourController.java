package com.kodat.of.halleyecommerce.product.attribute.colour;


import com.kodat.of.halleyecommerce.dto.product.attribute.colour.ColourDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/colours")
public class ColourController {

    private final ColourService colourService;

    @GetMapping
    public ResponseEntity<List<ColourDto>> findAll() {

        return ResponseEntity.ok(colourService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColourDto> findById(@PathVariable Long id) {

        return ResponseEntity.ok(colourService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ColourDto> findBySlug(@PathVariable String slug) {

        return ResponseEntity.ok(colourService.findBySlug(slug));
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<ColourDto> add(@RequestBody @Valid ColourDto colourDto, Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(colourService.add(colourDto,connectedUser));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication connectedUser) {

        colourService.deleteById(id,connectedUser);
        return ResponseEntity.noContent().build();
    }

}
