package com.universeodyssey.universe_odyssey.service.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universeodyssey.universe_odyssey.model.NewsArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NasaApiClient {

    private static final Logger logger = LoggerFactory.getLogger(NasaApiClient.class);

    @Value("${nasa.api.key}")
    private String apiKey;

    @Value("${news.nasa.apod.url:https://api.nasa.gov/planetary/apod}")
    private String apodUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NasaApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public List<NewsArticle> fetchApod() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            String url = apodUrl + "?api_key=" + apiKey;
            logger.info("Fetching NASA APOD: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode apodNode = objectMapper.readTree(response);

            NewsArticle article = mapApodToNewsArticle(apodNode);
            articles.add(article);

            logger.info("Successfully fetched NASA APOD");
        } catch (Exception e) {
            logger.error("Error fetching NASA APOD: {}", e.getMessage(), e);
        }

        return articles;
    }

    private NewsArticle mapApodToNewsArticle(JsonNode node) {
        NewsArticle article = new NewsArticle();

        article.setTitle(node.get("title").asText());
        article.setSummary(truncate(node.get("explanation").asText(), 500));
        article.setContent(node.get("explanation").asText());

        // Handle both image and video types
        String mediaType = node.has("media_type") ? node.get("media_type").asText() : "image";
        if ("image".equals(mediaType)) {
            article.setImageUrl(node.get("url").asText());
        } else if (node.has("thumbnail_url")) {
            article.setImageUrl(node.get("thumbnail_url").asText());
        } else {
            article.setImageUrl(""); // Video without thumbnail
        }

        article.setOriginalUrl("https://apod.nasa.gov/apod/");
        article.setSource("NASA");
        article.setCategory("Astronomy");

        // Parse date
        String dateStr = node.get("date").asText();
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        article.setPublishedDate(date.atStartOfDay());

        return article;
    }

    private String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
