package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogView;
import com.universeodyssey.universe_odyssey.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogViewRepository extends JpaRepository<BlogView, Long> {
    boolean existsByBlogAndUser(Blog blog, User user);
}
