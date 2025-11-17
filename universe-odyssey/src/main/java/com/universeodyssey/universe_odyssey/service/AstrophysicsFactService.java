package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.AstrophysicsFact;
import com.universeodyssey.universe_odyssey.repository.AstrophysicsFactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AstrophysicsFactService {

    private final AstrophysicsFactRepository factRepository;

    public AstrophysicsFactService(AstrophysicsFactRepository factRepository) {
        this.factRepository = factRepository;
    }

    public List<AstrophysicsFact> getAllFacts() {
        return factRepository.findAll();
    }

    public AstrophysicsFact saveFact(AstrophysicsFact fact) {
        return factRepository.save(fact);
    }

    public AstrophysicsFact getFactById(Long id) {
        return factRepository.findById(id).orElse(null);
    }

    public void deleteFact(Long id) {
        factRepository.deleteById(id);
    }
}
