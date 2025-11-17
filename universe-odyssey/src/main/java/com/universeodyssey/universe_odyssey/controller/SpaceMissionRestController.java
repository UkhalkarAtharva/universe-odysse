package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.SpaceMission;
import com.universeodyssey.universe_odyssey.repository.SpaceMissionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class SpaceMissionRestController {

    private final SpaceMissionRepository missionRepository;

    public SpaceMissionRestController(SpaceMissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @GetMapping
    public List<SpaceMission> getAllMissions() {
        return missionRepository.findAll();
    }

    @GetMapping("/{id}")
    public SpaceMission getMissionById(@PathVariable Long id) {
        return missionRepository.findById(id).orElse(null);
    }
}
