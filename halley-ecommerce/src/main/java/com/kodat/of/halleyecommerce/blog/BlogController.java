package com.kodat.of.halleyecommerce.blog;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.blog.BlogPostDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/blog")
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<BlogPostDto> add(@RequestBody @Valid BlogPostDto blogPostDto, Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.add(blogPostDto, connectedUser));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BlogPostDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {

        return ResponseEntity.ok(blogService.getAll(page,size,sortBy,sortDirection));
    }


    @GetMapping("/latest/{size}")
    public ResponseEntity<List<BlogPostDto>> getLatestBlogs(@PathVariable int size) {
        if (size <= 0) {
            size = 4;
        }
        List<BlogPostDto> latestBlogs = blogService.getLatestBlogs(size);
        return ResponseEntity.ok(latestBlogs);
    }

    @Secured("ADMIN")
    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<?> deleteById(@PathVariable Long blogPostId, Authentication connectedUser) {
        blogService.deleteById(blogPostId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<BlogPostDto> getById(@PathVariable String slug) {

        return ResponseEntity.ok(blogService.getById(slug));
    }

    @Secured("ADMIN")
    @PutMapping("/{blogPostId}")
    public ResponseEntity<BlogPostDto> updateById(@PathVariable Long blogPostId, @RequestBody BlogPostDto blogPostDto,
                                                  Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.updateById(blogPostId,blogPostDto,connectedUser));

    }


}
