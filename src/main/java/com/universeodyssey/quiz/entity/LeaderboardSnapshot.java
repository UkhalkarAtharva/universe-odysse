package com.universeodyssey.quiz.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leaderboard_snapshot")
public class LeaderboardSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "player_rank", nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private String tier;

    public LeaderboardSnapshot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
