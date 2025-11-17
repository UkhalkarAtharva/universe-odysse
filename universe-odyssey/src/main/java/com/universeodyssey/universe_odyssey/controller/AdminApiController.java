package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.PlanetDetailRepository;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
public class AdminApiController {

    private final PlanetDetailRepository planetRepo;
    private final UserRepository userRepo;

    public AdminApiController(PlanetDetailRepository planetRepo, UserRepository userRepo) {
        this.planetRepo = planetRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("totalPlanets", planetRepo.count());
        m.put("totalUsers", userRepo.count());
        // missions placeholder
        m.put("totalMissions", 0);
        m.put("lastUpdatedPlanet", planetRepo.findAll().stream().findFirst().orElse(null));
        return ResponseEntity.ok(m);
    }

    @GetMapping("/planets")
    public List<PlanetDetail> listPlanets() {
        return planetRepo.findAll();
    }

    @GetMapping("/planets/{id}")
    public ResponseEntity<?> getPlanet(@PathVariable Long id) {
        return planetRepo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/planets")
    public PlanetDetail createPlanet(@RequestBody PlanetDetail p) {
        return planetRepo.save(p);
    }

    @PutMapping("/planets/{id}")
    public ResponseEntity<?> updatePlanet(@PathVariable Long id, @RequestBody PlanetDetail p) {
        return planetRepo.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setMass(p.getMass());
            existing.setGravity(p.getGravity());
            existing.setMoons(p.getMoons());
            existing.setRadius(p.getRadius());
            existing.setDistanceFromSun(p.getDistanceFromSun());
            existing.setOrbitalPeriod(p.getOrbitalPeriod());
            existing.setSurfaceTemp(p.getSurfaceTemp());
            existing.setImageFilename(p.getImageFilename());
            existing.setShortDescription(p.getShortDescription());
            existing.setLongDescription(p.getLongDescription());
            existing.setAtmosphere(p.getAtmosphere());
            planetRepo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/planets/{id}")
    public ResponseEntity<?> deletePlanet(@PathVariable Long id) {
        return planetRepo.findById(id).map(p -> {
            planetRepo.delete(p);
            return ResponseEntity.ok(Map.of("success", true));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User u) {
        return userRepo.findById(id).map(existing -> {
            existing.setEmail(u.getEmail());
            existing.setUsername(u.getUsername());
            existing.setRole(u.getRole());
            existing.setActive(u.getActive());
            userRepo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepo.findById(id).map(u -> {
            userRepo.delete(u);
            return ResponseEntity.ok(Map.of("success", true));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
