package com.kodat.of.halleyecommerce.product.attribute.design;

import com.kodat.of.halleyecommerce.dto.product.attribute.colour.ColourDto;
import com.kodat.of.halleyecommerce.dto.product.attribute.design.DesignDto;
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
@RequestMapping("/designs")
public class DesignController {

    private final DesignService designService;

    @GetMapping
    public ResponseEntity<List<DesignDto>> findAll() {

        return ResponseEntity.ok(designService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignDto> findById(@PathVariable Long id) {

        return ResponseEntity.ok(designService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<DesignDto> findBySlug(@PathVariable String slug) {

        return ResponseEntity.ok(designService.findBySlug(slug));
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<DesignDto> add(@RequestBody @Valid DesignDto designDto, Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(designService.add(designDto,connectedUser));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication connectedUser) {

        designService.deleteById(id,connectedUser);
        return ResponseEntity.noContent().build();
    }

}
