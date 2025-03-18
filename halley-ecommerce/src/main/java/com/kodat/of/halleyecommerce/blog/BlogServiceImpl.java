package com.kodat.of.halleyecommerce.blog;


import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.blog.BlogPostDto;
import com.kodat.of.halleyecommerce.exception.BlogPostNotFoundException;
import com.kodat.of.halleyecommerce.mapper.blog.BlogMapper;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final SlugService slugService;
    private final RoleValidator roleValidator;

    @Override
    public BlogPostDto add(BlogPostDto blogPostDto, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        String slug = slugService.generateSlugWithNoCode(blogPostDto.getTitle());

        BlogPost blogPost = BlogMapper.toBlogPost(blogPostDto, slug);
        blogRepository.save(blogPost);

        return BlogMapper.toBlogPostDto(blogPost);
    }

    @Override
    public void deleteById(Long blogPostId, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);

        BlogPost blogPost = blogRepository.findById(blogPostId)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post not found to delete with id: " + blogPostId));

        blogRepository.delete(blogPost);
    }

    @Override
    public PageResponse<BlogPostDto> getAll(int page, int size, String sortBy, String sortDirection) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Page<BlogPost> blogPosts = blogRepository.findAll(pageable);


        if (blogPosts.isEmpty()) {
            throw new BlogPostNotFoundException("Not found any blog posts");
        }

        return createPageResponse(blogPosts);
    }

    @Override
    public BlogPostDto getById(String slug) {

        BlogPost blogPost = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post not found with id: " + slug));

        return BlogMapper.toBlogPostDto(blogPost);
    }

    @Override
    public BlogPostDto updateById(Long blogPostId, BlogPostDto blogPostDto, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        BlogPost existingBlogPost = blogRepository.findById(blogPostId)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post not found with id: " + blogPostId));

        String slug = slugService.generateSlugWithNoCode(blogPostDto.getTitle());

        BlogPost blogPost = BlogMapper.toBlogPostUpdate(existingBlogPost, blogPostDto, slug);
        blogRepository.save(blogPost);
        return BlogMapper.toBlogPostDto(blogPost);
    }

    @Override
    public List<BlogPostDto> getLatestBlogs(int size) {

        List<BlogPost> blogPosts = blogRepository.findTopNByOrderByCreatedDateDesc(size)
                .orElseThrow(() -> new BlogPostNotFoundException(size +" blog posts are not available"));

        return blogPosts.stream()
                .map(BlogMapper::toBlogPostDto)
                .toList();
    }


    public PageResponse<BlogPostDto> createPageResponse(Page<BlogPost> blogPosts) {
        List<BlogPostDto> blogPostDtos = blogPosts.stream()
                .map(BlogMapper::toBlogPostDto)
                .toList();

        return new PageResponse<>(
                blogPostDtos,
                blogPosts.getNumber(),
                blogPosts.getSize(),
                blogPosts.getTotalElements(),
                blogPosts.getTotalPages(),
                blogPosts.isFirst(),
                blogPosts.isLast()
        );
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
