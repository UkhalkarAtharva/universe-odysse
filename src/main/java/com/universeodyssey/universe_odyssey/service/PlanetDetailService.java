package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.repository.PlanetDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetDetailService {

    private final PlanetDetailRepository repo;

    public PlanetDetailService(PlanetDetailRepository repo) {
        this.repo = repo;
    }

    public List<PlanetDetail> findAll() {
        return repo.findAll();
    }

    public Optional<PlanetDetail> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<PlanetDetail> findByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    public PlanetDetail save(PlanetDetail p) {
        return repo.save(p);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
