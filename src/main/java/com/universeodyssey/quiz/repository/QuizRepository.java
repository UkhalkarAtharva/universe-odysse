package com.universeodyssey.quiz.repository;

import com.universeodyssey.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByDate(LocalDate date);

    List<Quiz> findByDateBefore(LocalDate date);
}
