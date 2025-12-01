package com.universeodyssey.quiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class QuizCorsConfig implements WebMvcConfigurer {
    @Value("${FRONTEND_URL:http://localhost:3000}")
    private String frontend;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/quiz/**").allowedOrigins(frontend).allowedMethods("GET","POST","OPTIONS").allowCredentials(true);
        registry.addMapping("/api/leaderboard/**").allowedOrigins(frontend).allowedMethods("GET","OPTIONS").allowCredentials(true);
    }
}
