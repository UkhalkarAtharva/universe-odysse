package com.universeodyssey.quiz.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_points")
public class UserPoints {
    @Id
    private Long userId;

    @Column(nullable = false)
    private Long totalPoints = 0L;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPoints() {}

    @PrePersist
    void onCreate() { updatedAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Long totalPoints) { this.totalPoints = totalPoints; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
