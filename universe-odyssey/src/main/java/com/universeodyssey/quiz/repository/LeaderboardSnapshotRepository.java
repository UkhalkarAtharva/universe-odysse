package com.universeodyssey.quiz.repository;

import com.universeodyssey.quiz.entity.LeaderboardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaderboardSnapshotRepository extends JpaRepository<LeaderboardSnapshot, Long> {
    @Query("SELECT l FROM LeaderboardSnapshot l WHERE l.snapshotDate = :date ORDER BY l.rank ASC")
    List<LeaderboardSnapshot> findBySnapshotDateOrderByRank(LocalDate date);
}
