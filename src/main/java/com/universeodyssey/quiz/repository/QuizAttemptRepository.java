package com.universeodyssey.quiz.repository;

import com.universeodyssey.quiz.entity.QuizAttempt;
import com.universeodyssey.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByQuizIdAndUserId(Long quizId, Long userId);

    long countByQuizId(Long quizId);

    void deleteByQuizId(Long quizId);
}
