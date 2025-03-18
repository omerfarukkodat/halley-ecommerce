package com.kodat.of.halleyecommerce.dto.blog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BlogPostDto {

    private Long id;

    private String title;

    private String content;

    private String slug;

    private String imageUrl;

    private LocalDateTime createdDate;
}
