package com.universeodyssey.universe_odyssey.service.news;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.universeodyssey.universe_odyssey.model.NewsArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class EsaRssFeedClient {

    private static final Logger logger = LoggerFactory.getLogger(EsaRssFeedClient.class);

    @Value("${news.esa.rss.url:https://www.esa.int/rssfeed/Our_Activities/Space_News}")
    private String rssUrl;

    @Value("${news.fetch.limit:50}")
    private int fetchLimit;

    public List<NewsArticle> fetchEsaNews() {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            logger.info("Fetching ESA RSS feed: {}", rssUrl);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(rssUrl)));

            List<SyndEntry> entries = feed.getEntries();
            int count = 0;

            for (SyndEntry entry : entries) {
                if (count >= fetchLimit)
                    break;

                try {
                    NewsArticle article = mapRssEntryToNewsArticle(entry);
                    articles.add(article);
                    count++;
                } catch (Exception e) {
                    logger.error("Error mapping RSS entry: {}", e.getMessage());
                }
            }

            logger.info("Successfully fetched {} articles from ESA RSS feed", articles.size());
        } catch (Exception e) {
            logger.error("Error fetching ESA RSS feed: {}", e.getMessage(), e);
        }

        return articles;
    }

    private NewsArticle mapRssEntryToNewsArticle(SyndEntry entry) {
        NewsArticle article = new NewsArticle();

        article.setTitle(entry.getTitle());
        article.setSummary(extractPlainText(entry.getDescription().getValue()));
        article.setContent(entry.getDescription().getValue());
        article.setOriginalUrl(entry.getLink());
        article.setSource("ESA");

        // Extract image from description HTML
        String imageUrl = extractImageFromHtml(entry.getDescription().getValue());
        article.setImageUrl(imageUrl);

        // Parse published date
        if (entry.getPublishedDate() != null) {
            article.setPublishedDate(
                    LocalDateTime.ofInstant(entry.getPublishedDate().toInstant(), ZoneId.systemDefault()));
        } else {
            article.setPublishedDate(LocalDateTime.now());
        }

        // Determine category
        article.setCategory(determineCategory(article.getTitle(), article.getSummary()));

        return article;
    }

    private String extractPlainText(String html) {
        if (html == null)
            return "";
        Document doc = Jsoup.parse(html);
        String text = doc.text();
        return text.length() > 1000 ? text.substring(0, 1000) + "..." : text;
    }

    private String extractImageFromHtml(String html) {
        if (html == null)
            return "";

        try {
            Document doc = Jsoup.parse(html);
            Element img = doc.select("img").first();
            if (img != null) {
                String src = img.attr("src");
                // Make sure URL is absolute
                if (src.startsWith("/")) {
                    return "https://www.esa.int" + src;
                }
                return src;
            }
        } catch (Exception e) {
            logger.error("Error extracting image from HTML: {}", e.getMessage());
        }

        return "";
    }

    private String determineCategory(String title, String summary) {
        String combined = (title + " " + summary).toLowerCase();

        if (combined.contains("launch") || combined.contains("rocket") || combined.contains("ariane")) {
            return "Launches";
        } else if (combined.contains("discover") || combined.contains("found") || combined.contains("detection")) {
            return "Discoveries";
        } else if (combined.contains("mission") || combined.contains("satellite") || combined.contains("spacecraft")) {
            return "Missions";
        } else if (combined.contains("research") || combined.contains("study") || combined.contains("science")) {
            return "Research";
        } else {
            return "General";
        }
    }
}
