package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlanetDetailRepository extends JpaRepository<PlanetDetail, Long> {
    Optional<PlanetDetail> findByNameIgnoreCase(String name);
}
