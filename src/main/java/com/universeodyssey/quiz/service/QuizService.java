package com.universeodyssey.quiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universeodyssey.quiz.dto.QuizDto;
import com.universeodyssey.quiz.dto.QuizQuestionDto;
import com.universeodyssey.quiz.dto.SubmitQuizRequest;
import com.universeodyssey.quiz.dto.SubmitQuizResponse;
import com.universeodyssey.quiz.entity.*;
import com.universeodyssey.quiz.repository.*;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class QuizService {
    private final Logger log = LoggerFactory.getLogger(QuizService.class);
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository questionRepository;
    private final QuizAttemptRepository attemptRepository;
    private final UserPointsRepository userPointsRepository;
    private final LeaderboardSnapshotRepository leaderboardRepository;
    private final GeminiClientService geminiClient;
    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public QuizService(QuizRepository quizRepository, QuizQuestionRepository questionRepository,
            QuizAttemptRepository attemptRepository, UserPointsRepository userPointsRepository,
            LeaderboardSnapshotRepository leaderboardRepository, GeminiClientService geminiClient,
            UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.userPointsRepository = userPointsRepository;
        this.leaderboardRepository = leaderboardRepository;
        this.geminiClient = geminiClient;
        this.userRepository = userRepository;
    }

    @Transactional
    public void regenerateTodayQuiz() {
        LocalDate today = LocalDate.now();
        Optional<Quiz> existingQuiz = quizRepository.findByDate(today);

        // Delete existing quiz and its questions
        if (existingQuiz.isPresent()) {
            Quiz quiz = existingQuiz.get();
            log.info("Deleting existing quiz for {} to regenerate", today);
            questionRepository.deleteByQuiz(quiz);
            attemptRepository.deleteByQuizId(quiz.getId());
            quizRepository.delete(quiz);
        }

        // Generate new quiz - throw exception if fails so we can see the error
        generateTodayQuizIfMissing(false);
    }

    @Transactional
    public void generateTodayQuizIfMissing() {
        generateTodayQuizIfMissing(true);
    }

    @Transactional
    public void generateTodayQuizIfMissing(boolean useFallback) {
        LocalDate today = LocalDate.now();
        if (quizRepository.findByDate(today).isPresent()) {
            return;
        }

        Quiz quiz = new Quiz();
        quiz.setDate(today);
        quiz.setTitle("Daily Quiz - " + today.toString());
        quiz = quizRepository.save(quiz);

        try {
            Optional<String> geminiQuizJson = geminiClient.generateQuiz(today);

            if (geminiQuizJson.isPresent()) {
                try {
                    JsonNode root = mapper.readTree(geminiQuizJson.get());
                    JsonNode questionsNode = root.path("quiz").path("questions");

                    if (questionsNode.isArray()) {
                        int index = 0;
                        for (JsonNode qNode : questionsNode) {
                            QuizQuestion q = new QuizQuestion();
                            q.setQuiz(quiz);
                            q.setQuestionText(qNode.path("question").asText("Question " + (++index)));
                            JsonNode options = qNode.path("options");
                            q.setOptions(options.isMissingNode() ? "[]" : options.toString());
                            q.setCorrectIndex(qNode.path("correct_index").asInt(0));
                            q.setPoints(qNode.path("points").asInt(10));
                            questionRepository.save(q);
                        }
                        log.info("Successfully generated and saved quiz for {}", today);
                        return;
                    }
                } catch (Exception e) {
                    log.error("Failed to parse Gemini quiz", e);
                    if (!useFallback)
                        throw new RuntimeException("Failed to parse Gemini quiz: " + e.getMessage(), e);
                }
            } else {
                if (!useFallback)
                    throw new RuntimeException("Gemini returned empty result (check logs for details)");
            }
        } catch (Exception e) {
            log.error("Gemini generation failed", e);
            if (!useFallback)
                throw e;
        }

        log.warn("Gemini failed to generate quiz, using fallback");
        createFallbackQuizQuestions(quiz);
    }

    private void createFallbackQuizQuestions(Quiz quiz) {
        for (int i = 1; i <= 5; i++) {
            QuizQuestion qq = new QuizQuestion();
            qq.setQuiz(quiz);
            qq.setQuestionText("Fallback Question " + i);
            qq.setOptions("[\"Option A\",\"Option B\",\"Option C\",\"Option D\"]");
            qq.setCorrectIndex(0);
            qq.setPoints(10);
            questionRepository.save(qq);
        }
    }

    public Optional<QuizDto> getTodayQuizForUser(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        Long userId = maybeUser.map(User::getId).orElse(null);
        LocalDate today = LocalDate.now();
        Optional<Quiz> maybeQuiz = quizRepository.findByDate(today);
        if (maybeQuiz.isEmpty())
            return Optional.empty();
        Quiz quiz = maybeQuiz.get();
        List<QuizQuestion> questions = questionRepository.findByQuiz(quiz);
        QuizDto dto = new QuizDto();
        dto.id = quiz.getId();
        dto.title = quiz.getTitle();
        dto.date = quiz.getDate();
        List<QuizQuestionDto> list = new ArrayList<>();
        for (QuizQuestion q : questions) {
            QuizQuestionDto qdto = new QuizQuestionDto();
            qdto.id = q.getId();
            qdto.questionText = q.getQuestionText();
            try {
                qdto.options = mapper.readValue(q.getOptions(), new TypeReference<List<String>>() {
                });
            } catch (Exception ex) {
                qdto.options = Arrays.asList(q.getOptions());
            }
            qdto.points = q.getPoints();
            list.add(qdto);
        }
        dto.questions = list;
        if (userId != null) {
            attemptRepository.findByQuizIdAndUserId(dto.id, userId).ifPresent(a -> {
                dto.completed = true;
                dto.previousScore = a.getScore();
            });
        }
        return Optional.of(dto);
    }

    @Transactional
    public SubmitQuizResponse submitAnswers(String userEmail, SubmitQuizRequest request) {
        Optional<User> maybeUser = userRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty())
            throw new IllegalArgumentException("User must exist");
        Long userId = maybeUser.get().getId();
        Optional<Quiz> maybeQuiz = quizRepository.findById(request.quizId);
        if (maybeQuiz.isEmpty())
            throw new IllegalArgumentException("Quiz not found");
        Quiz quiz = maybeQuiz.get();
        // duplicate check
        if (attemptRepository.findByQuizIdAndUserId(quiz.getId(), userId).isPresent()) {
            SubmitQuizResponse resp = new SubmitQuizResponse();
            resp.score = null;
            resp.totalPoints = null;
            resp.message = "Already submitted";
            return resp;
        }
        List<QuizQuestion> questions = questionRepository.findByQuiz(quiz);
        Map<Long, Integer> answers = request.answers == null ? new HashMap<>() : request.answers;
        int totalScore = 0;
        for (QuizQuestion q : questions) {
            Integer selected = answers.get(q.getId());
            if (selected != null && q.getCorrectIndex() != null && selected.equals(q.getCorrectIndex())) {
                totalScore += q.getPoints();
            }
        }
        // save attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setUserId(userId);
        attempt.setAnswers(serialize(answers));
        attempt.setScore(totalScore);
        attemptRepository.save(attempt);

        // update user points
        UserPoints up = userPointsRepository.findById(userId).orElseGet(() -> {
            UserPoints p = new UserPoints();
            p.setUserId(userId);
            p.setTotalPoints(0L);
            return p;
        });
        up.setTotalPoints(up.getTotalPoints() + totalScore);
        userPointsRepository.save(up);

        SubmitQuizResponse response = new SubmitQuizResponse();
        response.score = totalScore;
        response.totalPoints = up.getTotalPoints();
        response.message = "Submitted";
        return response;
    }

    private String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return "{}";
        }
    }
}
