package com.universeodyssey.universe_odyssey.service.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universeodyssey.universe_odyssey.model.NewsArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceflightNewsApiClient {

    private static final Logger logger = LoggerFactory.getLogger(SpaceflightNewsApiClient.class);

    @Value("${news.spaceflight.api.url:https://api.spaceflightnewsapi.net/v4/articles}")
    private String apiUrl;

    @Value("${news.fetch.limit:50}")
    private int fetchLimit;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SpaceflightNewsApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public List<NewsArticle> fetchLatestArticles() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            String url = apiUrl + "?limit=" + fetchLimit + "&ordering=-published_at";
            logger.info("Fetching articles from Spaceflight News API: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode articleNode : results) {
                    try {
                        NewsArticle article = mapToNewsArticle(articleNode);
                        articles.add(article);
                    } catch (Exception e) {
                        logger.error("Error mapping article: {}", e.getMessage());
                    }
                }
            }

            logger.info("Successfully fetched {} articles from Spaceflight News API", articles.size());
        } catch (Exception e) {
            logger.error("Error fetching from Spaceflight News API: {}", e.getMessage(), e);
        }

        return articles;
    }

    private NewsArticle mapToNewsArticle(JsonNode node) {
        NewsArticle article = new NewsArticle();

        article.setTitle(node.get("title").asText());
        article.setSummary(node.get("summary").asText());
        article.setContent(node.get("summary").asText()); // Spaceflight API doesn't provide full content
        article.setImageUrl(node.get("image_url").asText());
        article.setOriginalUrl(node.get("url").asText());
        article.setSource("Spaceflight");

        // Parse published date
        String publishedAt = node.get("published_at").asText();
        article.setPublishedDate(ZonedDateTime.parse(publishedAt).toLocalDateTime());

        // Determine category based on keywords
        article.setCategory(determineCategory(article.getTitle(), article.getSummary()));

        return article;
    }

    private String determineCategory(String title, String summary) {
        String combined = (title + " " + summary).toLowerCase();

        if (combined.contains("launch") || combined.contains("rocket") || combined.contains("liftoff")) {
            return "Launches";
        } else if (combined.contains("discover") || combined.contains("found") || combined.contains("detection")) {
            return "Discoveries";
        } else if (combined.contains("mission") || combined.contains("spacecraft") || combined.contains("probe")) {
            return "Missions";
        } else if (combined.contains("research") || combined.contains("study") || combined.contains("science")) {
            return "Research";
        } else {
            return "General";
        }
    }
}
