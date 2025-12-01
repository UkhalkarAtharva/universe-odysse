package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.service.PlanetDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetDetailService service;

    public PlanetController(PlanetDetailService service) {
        this.service = service;
    }

    @GetMapping("/{name}")
    public String getPlanetDetail(@PathVariable String name, Model model) {
        return service.findByName(name)
                .map(planet -> {
                    model.addAttribute("planet", planet);
                    return "planet-detail";
                })
                .orElse("error/404");
    }
}
