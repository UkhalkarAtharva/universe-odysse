package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findByBlogOrderByCreatedAtDesc(Blog blog);

    Page<BlogComment> findByBlogOrderByCreatedAtDesc(Blog blog, Pageable pageable);

    long countByBlog(Blog blog);
}
