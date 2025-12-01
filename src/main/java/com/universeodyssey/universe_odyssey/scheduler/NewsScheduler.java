package com.universeodyssey.universe_odyssey.scheduler;

import com.universeodyssey.universe_odyssey.service.news.NewsAggregatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NewsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NewsScheduler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final NewsAggregatorService newsAggregatorService;

    public NewsScheduler(NewsAggregatorService newsAggregatorService) {
        this.newsAggregatorService = newsAggregatorService;
    }

    // Run every 30 minutes: "0 */30 * * * *"
    // For testing, you can use "0 * * * * *" (every minute) or "0 0/5 * * * *"
    // (every 5 minutes)
    @Scheduled(cron = "0 */30 * * * *")
    public void fetchNewsScheduled() {
        String startTime = LocalDateTime.now().format(formatter);
        logger.info("========================================");
        logger.info("Starting scheduled news fetch at {}", startTime);
        logger.info("========================================");

        try {
            long startMillis = System.currentTimeMillis();

            int savedCount = newsAggregatorService.aggregateAllNews();

            long duration = System.currentTimeMillis() - startMillis;
            String endTime = LocalDateTime.now().format(formatter);

            logger.info("========================================");
            logger.info("Scheduled news fetch completed at {}", endTime);
            logger.info("Duration: {} ms", duration);
            logger.info("New articles saved: {}", savedCount);
            logger.info("========================================");

        } catch (Exception e) {
            logger.error("========================================");
            logger.error("Error during scheduled news fetch: {}", e.getMessage(), e);
            logger.error("========================================");
        }
    }

    // Run on application startup to fetch initial news
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE) // Run once 10 seconds after startup
    public void fetchNewsOnStartup() {
        logger.info("Fetching initial news on application startup...");
        try {
            newsAggregatorService.aggregateAllNews();
            logger.info("Initial news fetch completed successfully");
        } catch (Exception e) {
            logger.error("Error during initial news fetch: {}", e.getMessage(), e);
        }
    }
}
