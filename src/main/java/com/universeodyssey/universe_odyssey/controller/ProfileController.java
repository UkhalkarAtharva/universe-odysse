package com.universeodyssey.universe_odyssey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profilePage() {
        return "forward:/profile/index.html";
    }
}
