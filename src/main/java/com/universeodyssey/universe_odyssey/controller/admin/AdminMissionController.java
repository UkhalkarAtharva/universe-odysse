package com.universeodyssey.universe_odyssey.controller.admin;

import com.universeodyssey.universe_odyssey.model.SpaceMission;
import com.universeodyssey.universe_odyssey.service.SpaceMissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/missions")
public class AdminMissionController {

    private final SpaceMissionService missionService;

    public AdminMissionController(SpaceMissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public String listMissions(Model model) {
        model.addAttribute("missions", missionService.getAllMissions());
        return "admin/mission-list";
    }

    @GetMapping("/new")
    public String showNewMissionForm(Model model) {
        model.addAttribute("mission", new SpaceMission());
        return "admin/mission-form";
    }

    @PostMapping("/save")
    public String saveMission(@ModelAttribute SpaceMission mission) {
        missionService.saveMission(mission);
        return "redirect:/admin/missions";
    }

    @GetMapping("/edit/{id}")
    public String editMission(@PathVariable Long id, Model model) {
        model.addAttribute("mission", missionService.getMissionById(id));
        return "admin/mission-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return "redirect:/admin/missions";
    }
}
