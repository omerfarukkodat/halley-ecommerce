package com.kodat.of.halleyecommerce.blog;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.blog.BlogPostDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BlogService {

    BlogPostDto add(BlogPostDto blogPostDto, Authentication connectedUser);

    void deleteById(Long blogPostId, Authentication connectedUser);

    PageResponse<BlogPostDto> getAll(int page, int size, String sortBy, String sortDirection);

    BlogPostDto getById(String slug);

    BlogPostDto updateById(Long blogPostId, BlogPostDto blogPostDto, Authentication connectedUser);

    List<BlogPostDto> getLatestBlogs(int size);
}
