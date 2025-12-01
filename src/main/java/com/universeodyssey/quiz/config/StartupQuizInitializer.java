package com.universeodyssey.quiz.config;

import com.universeodyssey.quiz.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupQuizInitializer {
    private final Logger log = LoggerFactory.getLogger(StartupQuizInitializer.class);
    private final QuizService quizService;

    public StartupQuizInitializer(QuizService quizService) { this.quizService = quizService; }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Initializing today's quiz at startup");
        quizService.generateTodayQuizIfMissing();
    }
}
