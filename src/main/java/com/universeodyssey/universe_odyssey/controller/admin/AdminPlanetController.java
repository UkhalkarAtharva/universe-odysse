package com.universeodyssey.universe_odyssey.controller.admin;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.service.PlanetDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/planets")
public class AdminPlanetController {

    private final PlanetDetailService planetService;

    public AdminPlanetController(PlanetDetailService planetService) {
        this.planetService = planetService;
    }

    @GetMapping
    public String listPlanets(Model model) {
        model.addAttribute("planets", planetService.findAll());
        return "admin/planet-list";
    }

    @GetMapping("/new")
    public String newPlanet(Model model) {
        model.addAttribute("planet", new PlanetDetail());
        return "admin/planet-form";
    }

    @PostMapping("/save")
    public String savePlanet(@ModelAttribute PlanetDetail planet) {
        planetService.save(planet);
        return "redirect:/admin/planets";
    }

    @GetMapping("/edit/{id}")
    public String editPlanet(@PathVariable Long id, Model model) {
        planetService.findById(id).ifPresent(p -> model.addAttribute("planet", p));
        return "admin/planet-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlanet(@PathVariable Long id) {
        planetService.deleteById(id);
        return "redirect:/admin/planets";
    }
}
