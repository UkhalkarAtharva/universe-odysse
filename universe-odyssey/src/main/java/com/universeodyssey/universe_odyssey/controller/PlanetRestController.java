package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.dto.PlanetDetailResponse;
import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.service.PlanetDetailService;
import com.universeodyssey.universe_odyssey.service.ExternalPlanetDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetRestController {

    private final PlanetDetailService service;
    private final ExternalPlanetDataService externalPlanetDataService;

    public PlanetRestController(PlanetDetailService service, ExternalPlanetDataService externalPlanetDataService) {
        this.service = service;
        this.externalPlanetDataService = externalPlanetDataService;
    }

    @GetMapping
    public List<PlanetDetail> all() {
        return service.findAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<PlanetDetail> byId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<PlanetDetail> byName(@PathVariable String name) {
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Public API: GET /api/planets/{name} -> returns unified PlanetDetailResponse
    @GetMapping("/{name}")
    public ResponseEntity<PlanetDetailResponse> getPlanetData(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) return ResponseEntity.badRequest().build();
        PlanetDetailResponse data = externalPlanetDataService.getPlanetData(name.trim());
        if (data != null && Boolean.TRUE.equals(data.getFound())) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }

    // Admin endpoints (save/delete) - admin secured later
    @PostMapping
    public PlanetDetail create(@RequestBody PlanetDetail p) {
        return service.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

