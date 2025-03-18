package com.kodat.of.halleyecommerce.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<BlogPost, Long> {

    @Query("SELECT b FROM BlogPost b ORDER BY b.createdDate DESC LIMIT :limit")
    Optional<List<BlogPost>> findTopNByOrderByCreatedDateDesc(int limit);

    Optional<BlogPost> findBySlug(String slug);
}
