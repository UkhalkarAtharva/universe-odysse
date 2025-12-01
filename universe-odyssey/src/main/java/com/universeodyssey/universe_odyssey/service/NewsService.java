package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.NewsArticle;
import com.universeodyssey.universe_odyssey.repository.NewsArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsArticleRepository newsRepository;

    public NewsService(NewsArticleRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<NewsArticle> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedDate").descending());
        return newsRepository.findAllByOrderByPublishedDateDesc(pageable);
    }

    public Page<NewsArticle> getNewsBySource(String source, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findBySourceOrderByPublishedDateDesc(source, pageable);
    }

    public Page<NewsArticle> getNewsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategoryOrderByPublishedDateDesc(category, pageable);
    }

    public Page<NewsArticle> getNewsBySourceAndCategory(String source, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findBySourceAndCategoryOrderByPublishedDateDesc(source, category, pageable);
    }

    public Optional<NewsArticle> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public List<NewsArticle> getLatestNews(int limit) {
        if (limit == 5) {
            return newsRepository.findTop5ByOrderByPublishedDateDesc();
        }
        Pageable pageable = PageRequest.of(0, limit, Sort.by("publishedDate").descending());
        return newsRepository.findAllByOrderByPublishedDateDesc(pageable).getContent();
    }

    public List<String> getAvailableSources() {
        return Arrays.asList("NASA", "ESA", "ISRO", "Spaceflight");
    }

    public List<String> getAvailableCategories() {
        return Arrays.asList("Launches", "Discoveries", "Missions", "Research", "Astronomy", "General");
    }
}
