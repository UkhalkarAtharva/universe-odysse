package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogLike;
import com.universeodyssey.universe_odyssey.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {
    long countByBlog(Blog blog);

    boolean existsByBlogAndUser(Blog blog, User user);

    Optional<BlogLike> findByBlogAndUser(Blog blog, User user);

    void deleteByBlogAndUser(Blog blog, User user);
}
