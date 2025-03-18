package com.kodat.of.halleyecommerce.mapper.blog;

import com.kodat.of.halleyecommerce.blog.BlogPost;
import com.kodat.of.halleyecommerce.dto.blog.BlogPostDto;

public class BlogMapper {


    public static BlogPostDto toBlogPostDto(BlogPost blogPost) {

        return BlogPostDto.builder()
                .id(blogPost.getId())
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .slug(blogPost.getSlug())
                .imageUrl(blogPost.getImageUrl())
                .createdDate(blogPost.getCreatedDate())
                .build();
    }

    public static BlogPost toBlogPost(BlogPostDto blogPostDto , String slug) {

        return BlogPost.builder()
                .title(blogPostDto.getTitle())
                .content(blogPostDto.getContent())
                .slug(slug)
                .imageUrl(blogPostDto.getImageUrl())
                .build();
    }

    public static BlogPost toBlogPostUpdate (BlogPost existingBlogPost ,BlogPostDto blogPostDto , String slug) {

        existingBlogPost.setTitle(blogPostDto.getTitle());
        existingBlogPost.setContent(blogPostDto.getContent());
        existingBlogPost.setImageUrl(blogPostDto.getImageUrl());
        existingBlogPost.setSlug(slug);
        return existingBlogPost;




    }

}
