package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {

    // Find all news with pagination, sorted by published date descending
    Page<NewsArticle> findAllByOrderByPublishedDateDesc(Pageable pageable);

    // Filter by source
    Page<NewsArticle> findBySourceOrderByPublishedDateDesc(String source, Pageable pageable);

    // Filter by category
    Page<NewsArticle> findByCategoryOrderByPublishedDateDesc(String category, Pageable pageable);

    // Filter by both source and category
    Page<NewsArticle> findBySourceAndCategoryOrderByPublishedDateDesc(String source, String category,
            Pageable pageable);

    // Check if article already exists by URL (for duplicate detection)
    boolean existsByOriginalUrl(String originalUrl);

    // Find article by URL
    Optional<NewsArticle> findByOriginalUrl(String originalUrl);

    // Get latest N articles for home page widget
    List<NewsArticle> findTop5ByOrderByPublishedDateDesc();
}
