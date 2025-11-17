package com.universeodyssey.universe_odyssey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.universeodyssey.universe_odyssey.model.QuizQuestion;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}
