package com.universeodyssey.universe_odyssey.service.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universeodyssey.universe_odyssey.model.NewsArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
public class IsroNewsScraperClient {

    private static final Logger logger = LoggerFactory.getLogger(IsroNewsScraperClient.class);

    @Value("${news.isro.url:https://www.isro.gov.in/PressRelease.html}")
    private String isroUrl;

    @Value("${news.spaceflight.api.url:https://api.spaceflightnewsapi.net/v4/articles}")
    private String spaceflightApiUrl;

    @Value("${news.fetch.limit:50}")
    private int fetchLimit;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IsroNewsScraperClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public List<NewsArticle> fetchIsroNews() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            logger.info("Attempting to scrape ISRO website: {}", isroUrl);
            articles = scrapeIsroWebsite();

            if (articles.isEmpty()) {
                logger.warn("Scraping failed or returned no results, falling back to Spaceflight API");
                articles = fallbackToSpaceflightApi();
            } else {
                logger.info("Successfully scraped {} articles from ISRO website", articles.size());
            }
        } catch (Exception e) {
            logger.error("Error fetching ISRO news, using fallback: {}", e.getMessage());
            articles = fallbackToSpaceflightApi();
        }

        return articles;
    }

    private List<NewsArticle> scrapeIsroWebsite() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(isroUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            // ISRO press releases are typically in a table or list
            Elements pressReleases = doc.select("table tr, .press-release-item, article");

            int count = 0;
            for (Element release : pressReleases) {
                if (count >= fetchLimit)
                    break;

                try {
                    // Try to extract title and link
                    Element link = release.select("a").first();
                    if (link != null) {
                        String title = link.text().trim();
                        String url = link.attr("abs:href");

                        if (!title.isEmpty() && !url.isEmpty()) {
                            NewsArticle article = new NewsArticle();
                            article.setTitle(title);
                            article.setSummary(title); // Use title as summary for now
                            article.setContent(title);
                            article.setOriginalUrl(url);
                            article.setSource("ISRO");
                            article.setCategory(determineCategoryFromTitle(title));
                            article.setPublishedDate(LocalDateTime.now()); // ISRO doesn't always provide dates
                            article.setImageUrl(""); // ISRO press releases typically don't have images

                            articles.add(article);
                            count++;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Error parsing ISRO press release element: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error scraping ISRO website: {}", e.getMessage());
        }

        return articles;
    }

    private List<NewsArticle> fallbackToSpaceflightApi() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            // Search for ISRO-related articles in Spaceflight News API
            String url = spaceflightApiUrl + "?limit=" + fetchLimit + "&search=ISRO&ordering=-published_at";
            logger.info("Fetching ISRO news from Spaceflight API fallback: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode articleNode : results) {
                    try {
                        NewsArticle article = mapSpaceflightToNewsArticle(articleNode);
                        articles.add(article);
                    } catch (Exception e) {
                        logger.error("Error mapping fallback article: {}", e.getMessage());
                    }
                }
            }

            logger.info("Successfully fetched {} ISRO articles from Spaceflight API fallback", articles.size());
        } catch (Exception e) {
            logger.error("Error in Spaceflight API fallback: {}", e.getMessage());
        }

        return articles;
    }

    private NewsArticle mapSpaceflightToNewsArticle(JsonNode node) {
        NewsArticle article = new NewsArticle();

        article.setTitle(node.get("title").asText());
        article.setSummary(node.get("summary").asText());
        article.setContent(node.get("summary").asText());
        article.setImageUrl(node.get("image_url").asText());
        article.setOriginalUrl(node.get("url").asText());
        article.setSource("ISRO"); // Override source to ISRO

        String publishedAt = node.get("published_at").asText();
        article.setPublishedDate(ZonedDateTime.parse(publishedAt).toLocalDateTime());

        article.setCategory(determineCategoryFromTitle(article.getTitle()));

        return article;
    }

    private String determineCategoryFromTitle(String title) {
        String lower = title.toLowerCase();

        if (lower.contains("launch") || lower.contains("pslv") || lower.contains("gslv")) {
            return "Launches";
        } else if (lower.contains("mission") || lower.contains("satellite") || lower.contains("chandrayaan")
                || lower.contains("gaganyaan")) {
            return "Missions";
        } else if (lower.contains("test") || lower.contains("research")) {
            return "Research";
        } else {
            return "General";
        }
    }
}
