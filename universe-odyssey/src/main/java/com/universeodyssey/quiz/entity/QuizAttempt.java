package com.universeodyssey.quiz.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempt", uniqueConstraints = {@UniqueConstraint(columnNames = {"quiz_id", "user_id"})})
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String answers; // JSON mapping questionId -> index

    @Column(nullable = false)
    private Integer score = 0;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;

    public QuizAttempt() {}

    @PrePersist
    void onCreate() { attemptedAt = LocalDateTime.now(); }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAnswers() { return answers; }
    public void setAnswers(String answers) { this.answers = answers; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public LocalDateTime getAttemptedAt() { return attemptedAt; }
}
