package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.SpaceMission;
import com.universeodyssey.universe_odyssey.repository.SpaceMissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceMissionService {

    private final SpaceMissionRepository missionRepository;

    public SpaceMissionService(SpaceMissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public List<SpaceMission> getAllMissions() {
        return missionRepository.findAll();
    }

    public SpaceMission getMissionById(Long id) {
        return missionRepository.findById(id).orElse(null);
    }

    public SpaceMission saveMission(SpaceMission mission) {
        return missionRepository.save(mission);
    }

    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }
}
