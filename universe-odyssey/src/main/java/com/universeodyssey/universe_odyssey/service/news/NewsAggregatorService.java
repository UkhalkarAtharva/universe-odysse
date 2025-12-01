package com.universeodyssey.universe_odyssey.service.news;

import com.universeodyssey.universe_odyssey.model.NewsArticle;
import com.universeodyssey.universe_odyssey.repository.NewsArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsAggregatorService {

    private static final Logger logger = LoggerFactory.getLogger(NewsAggregatorService.class);

    private final SpaceflightNewsApiClient spaceflightClient;
    private final NasaApiClient nasaClient;
    private final EsaRssFeedClient esaClient;
    private final IsroNewsScraperClient isroClient;
    private final NewsArticleRepository newsRepository;

    public NewsAggregatorService(
            SpaceflightNewsApiClient spaceflightClient,
            NasaApiClient nasaClient,
            EsaRssFeedClient esaClient,
            IsroNewsScraperClient isroClient,
            NewsArticleRepository newsRepository) {
        this.spaceflightClient = spaceflightClient;
        this.nasaClient = nasaClient;
        this.esaClient = esaClient;
        this.isroClient = isroClient;
        this.newsRepository = newsRepository;
    }

    public int aggregateAllNews() {
        logger.info("Starting news aggregation from all sources...");

        List<NewsArticle> allArticles = new ArrayList<>();

        // Fetch from Spaceflight News API
        try {
            List<NewsArticle> spaceflightArticles = spaceflightClient.fetchLatestArticles();
            allArticles.addAll(spaceflightArticles);
            logger.info("Fetched {} articles from Spaceflight News API", spaceflightArticles.size());
        } catch (Exception e) {
            logger.error("Failed to fetch from Spaceflight News API: {}", e.getMessage());
        }

        // Fetch from NASA APOD
        try {
            List<NewsArticle> nasaArticles = nasaClient.fetchApod();
            allArticles.addAll(nasaArticles);
            logger.info("Fetched {} articles from NASA APOD", nasaArticles.size());
        } catch (Exception e) {
            logger.error("Failed to fetch from NASA APOD: {}", e.getMessage());
        }

        // Fetch from ESA RSS
        try {
            List<NewsArticle> esaArticles = esaClient.fetchEsaNews();
            allArticles.addAll(esaArticles);
            logger.info("Fetched {} articles from ESA RSS", esaArticles.size());
        } catch (Exception e) {
            logger.error("Failed to fetch from ESA RSS: {}", e.getMessage());
        }

        // Fetch from ISRO
        try {
            List<NewsArticle> isroArticles = isroClient.fetchIsroNews();
            allArticles.addAll(isroArticles);
            logger.info("Fetched {} articles from ISRO", isroArticles.size());
        } catch (Exception e) {
            logger.error("Failed to fetch from ISRO: {}", e.getMessage());
        }

        // Save new articles (skip duplicates)
        int savedCount = saveNewsArticles(allArticles);

        logger.info("News aggregation complete. Total fetched: {}, New articles saved: {}",
                allArticles.size(), savedCount);

        return savedCount;
    }

    private int saveNewsArticles(List<NewsArticle> articles) {
        int savedCount = 0;

        for (NewsArticle article : articles) {
            try {
                if (saveArticle(article)) {
                    savedCount++;
                }
            } catch (Exception e) {
                logger.error("Error processing article '{}': {}", article.getTitle(), e.getMessage());
            }
        }

        return savedCount;
    }

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public boolean saveArticle(NewsArticle article) {
        try {
            if (!isDuplicate(article)) {
                newsRepository.save(article);
                logger.debug("Saved new article: {}", article.getTitle());
                return true;
            } else {
                logger.debug("Skipping duplicate article: {}", article.getTitle());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error saving article '{}': {}", article.getTitle(), e.getMessage(), e);
            throw e; // Re-throw to trigger rollback for this specific transaction if needed, though
                     // caught above
        }
    }

    private boolean isDuplicate(NewsArticle article) {
        return newsRepository.existsByOriginalUrl(article.getOriginalUrl());
    }
}
