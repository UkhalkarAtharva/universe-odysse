package com.universeodyssey.quiz.scheduler;

import com.universeodyssey.quiz.entity.Quiz;
import com.universeodyssey.quiz.repository.QuizAttemptRepository;
import com.universeodyssey.quiz.repository.QuizQuestionRepository;
import com.universeodyssey.quiz.repository.QuizRepository;
import com.universeodyssey.quiz.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class QuizGeneratorScheduler {
    private final Logger log = LoggerFactory.getLogger(QuizGeneratorScheduler.class);
    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository questionRepository;
    private final QuizAttemptRepository attemptRepository;

    public QuizGeneratorScheduler(QuizService quizService, QuizRepository quizRepository,
            QuizQuestionRepository questionRepository, QuizAttemptRepository attemptRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    /**
     * Runs daily at 3 AM to:
     * 1. Delete quizzes older than 7 days
     * 2. Generate today's quiz if missing
     */
    @Scheduled(cron = "0 0 3 * * *") // Every day at 3 AM
    @Transactional
    public void generateDailyQuiz() {
        log.info("Running daily quiz maintenance at 3 AM...");

        // Delete old quizzes first
        deleteOldQuizzes();

        // Generate today's quiz
        log.info("Generating today's quiz");
        quizService.generateTodayQuizIfMissing();

        log.info("Daily quiz maintenance completed");
    }

    /**
     * Delete quizzes older than 7 days to keep database clean
     */
    private void deleteOldQuizzes() {
        LocalDate cutoffDate = LocalDate.now().minusDays(7);
        List<Quiz> oldQuizzes = quizRepository.findByDateBefore(cutoffDate);

        if (!oldQuizzes.isEmpty()) {
            log.info("Deleting {} old quizzes (before {})", oldQuizzes.size(), cutoffDate);

            for (Quiz quiz : oldQuizzes) {
                try {
                    questionRepository.deleteByQuiz(quiz);
                    attemptRepository.deleteByQuizId(quiz.getId());
                    quizRepository.delete(quiz);
                } catch (Exception e) {
                    log.error("Error deleting quiz {}: {}", quiz.getId(), e.getMessage());
                }
            }

            log.info("Successfully deleted old quizzes");
        } else {
            log.debug("No old quizzes to delete");
        }
    }

    /**
     * Run on application startup to ensure today's quiz exists
     * Runs 10 seconds after startup to allow database connections to initialize
     * Also checks if today's quiz has fallback questions and regenerates if needed
     */
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    @Transactional
    public void generateQuizOnStartup() {
        log.info("Checking for today's quiz on application startup...");

        LocalDate today = LocalDate.now();
        Optional<Quiz> existingQuiz = quizRepository.findByDate(today);

        if (existingQuiz.isPresent()) {
            // Check if it has fallback questions
            Quiz quiz = existingQuiz.get();
            List<com.universeodyssey.quiz.entity.QuizQuestion> questions = questionRepository.findByQuiz(quiz);

            boolean hasFallbackQuestions = questions.stream()
                    .anyMatch(q -> q.getQuestionText() != null && q.getQuestionText().startsWith("Fallback Question"));

            if (hasFallbackQuestions) {
                log.warn("Today's quiz has fallback questions - regenerating with Gemini API...");
                try {
                    // Delete fallback quiz
                    questionRepository.deleteByQuiz(quiz);
                    attemptRepository.deleteByQuizId(quiz.getId());
                    quizRepository.delete(quiz);

                    // Generate new quiz with Gemini
                    quizService.generateTodayQuizIfMissing();
                    log.info("Successfully regenerated today's quiz with real Gemini questions");
                } catch (Exception e) {
                    log.error("Failed to regenerate quiz: {}", e.getMessage(), e);
                }
            } else {
                log.info("Today's quiz already exists with real questions");
            }
        } else {
            // No quiz exists, generate it
            quizService.generateTodayQuizIfMissing();
        }

        log.info("Startup quiz check completed");
    }
}
