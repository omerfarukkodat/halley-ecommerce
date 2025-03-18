package com.kodat.of.halleyecommerce.showcase;

import com.kodat.of.halleyecommerce.dto.showcase.ShowcaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/showcase")
public class ShowcaseController {

    private final ShowcaseService showcaseService;

    @GetMapping
    public ResponseEntity<List<ShowcaseDto>> getAll() {

        return ResponseEntity.ok(showcaseService.getAll());

    }
}
