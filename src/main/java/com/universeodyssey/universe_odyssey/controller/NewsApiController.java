package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.NewsArticle;
import com.universeodyssey.universe_odyssey.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsApiController {

    private final NewsService newsService;

    public NewsApiController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<NewsArticle>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String category) {

        Page<NewsArticle> newsPage;

        if (source != null && category != null) {
            newsPage = newsService.getNewsBySourceAndCategory(source, category, page, size);
        } else if (source != null) {
            newsPage = newsService.getNewsBySource(source, page, size);
        } else if (category != null) {
            newsPage = newsService.getNewsByCategory(category, page, size);
        } else {
            newsPage = newsService.getAllNews(page, size);
        }

        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticle> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<NewsArticle>> getLatestNews(
            @RequestParam(defaultValue = "5") int limit) {
        List<NewsArticle> latestNews = newsService.getLatestNews(limit);
        return ResponseEntity.ok(latestNews);
    }

    @GetMapping("/sources")
    public ResponseEntity<List<String>> getAvailableSources() {
        return ResponseEntity.ok(newsService.getAvailableSources());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        return ResponseEntity.ok(newsService.getAvailableCategories());
    }
}
