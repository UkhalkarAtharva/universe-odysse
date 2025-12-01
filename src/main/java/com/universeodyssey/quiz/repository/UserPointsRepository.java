package com.universeodyssey.quiz.repository;

import com.universeodyssey.quiz.entity.UserPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {
	List<UserPoints> findTop100ByOrderByTotalPointsDesc();
}
