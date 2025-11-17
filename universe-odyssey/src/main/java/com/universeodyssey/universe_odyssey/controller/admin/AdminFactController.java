package com.universeodyssey.universe_odyssey.controller.admin;

import com.universeodyssey.universe_odyssey.model.AstrophysicsFact;
import com.universeodyssey.universe_odyssey.service.AstrophysicsFactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/facts")
public class AdminFactController {

    private final AstrophysicsFactService factService;

    public AdminFactController(AstrophysicsFactService factService) {
        this.factService = factService;
    }

    @GetMapping
    public String listFacts(Model model) {
        model.addAttribute("facts", factService.getAllFacts());
        return "admin/facts-list";
    }

    @GetMapping("/new")
    public String showNewFactForm(Model model) {
        model.addAttribute("fact", new AstrophysicsFact());
        return "admin/fact-form";
    }

    @PostMapping("/save")
    public String saveFact(@ModelAttribute AstrophysicsFact fact) {
        factService.saveFact(fact);
        return "redirect:/admin/facts";
    }

    @GetMapping("/edit/{id}")
    public String editFact(@PathVariable Long id, Model model) {
        model.addAttribute("fact", factService.getFactById(id));
        return "admin/fact-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteFact(@PathVariable Long id) {
        factService.deleteFact(id);
        return "redirect:/admin/facts";
    }
}
