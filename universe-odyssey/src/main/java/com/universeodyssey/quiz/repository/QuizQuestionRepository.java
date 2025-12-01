package com.universeodyssey.quiz.repository;

import com.universeodyssey.quiz.entity.QuizQuestion;
import com.universeodyssey.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByQuiz(Quiz quiz);

    void deleteByQuiz(Quiz quiz);
}
