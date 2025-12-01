package com.universeodyssey.quiz.service;

import com.universeodyssey.quiz.dto.SubmitQuizRequest;
import com.universeodyssey.quiz.dto.SubmitQuizResponse;
import com.universeodyssey.quiz.entity.Quiz;
import com.universeodyssey.quiz.entity.QuizQuestion;
import com.universeodyssey.quiz.repository.QuizQuestionRepository;
import com.universeodyssey.quiz.repository.QuizRepository;
import com.universeodyssey.quiz.repository.QuizAttemptRepository;
import com.universeodyssey.quiz.repository.UserPointsRepository;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class QuizServiceTest {

    @Autowired QuizService quizService;
    @Autowired QuizRepository quizRepository;
    @Autowired QuizQuestionRepository questionRepository;
    @Autowired UserRepository userRepository;
    @Autowired QuizAttemptRepository attemptRepository;
    @Autowired UserPointsRepository userPointsRepository;

    @Test
    @Transactional
    public void submitAnswers_happyPath_and_duplicateAttempt() {
        // create a user
        User u = new User(); u.setFullName("Test User"); u.setEmail("testuser@example.com"); u.setPassword("x");
        userRepository.save(u);
        // create quiz
        Quiz quiz = new Quiz(); quiz.setDate(LocalDate.now()); quiz.setTitle("Test Quiz"); quiz = quizRepository.save(quiz);
        // create questions
        QuizQuestion q1 = new QuizQuestion(); q1.setQuiz(quiz); q1.setQuestionText("Q1"); q1.setOptions("[\"a\",\"b\"]"); q1.setCorrectIndex(1); q1.setPoints(10); questionRepository.save(q1);
        QuizQuestion q2 = new QuizQuestion(); q2.setQuiz(quiz); q2.setQuestionText("Q2"); q2.setOptions("[\"a\",\"b\"]"); q2.setCorrectIndex(0); q2.setPoints(20); questionRepository.save(q2);

        SubmitQuizRequest req = new SubmitQuizRequest(); req.quizId = quiz.getId(); Map<Long,Integer> answers = new HashMap<>(); answers.put(q1.getId(), 1); answers.put(q2.getId(), 1); req.answers = answers;
        // submit first time
        SubmitQuizResponse r1 = quizService.submitAnswers(u.getEmail(), req);
        Assertions.assertNotNull(r1); Assertions.assertEquals(10, r1.score); // only q1 correct
        // submit duplicate
        SubmitQuizResponse r2 = quizService.submitAnswers(u.getEmail(), req);
        Assertions.assertNotNull(r2); // expects Already submitted message
        Assertions.assertEquals("Already submitted", r2.message);
    }
}
