package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByStatusOrderByCreatedAtDesc(Blog.BlogStatus status, Pageable pageable);

    Page<Blog> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
}
