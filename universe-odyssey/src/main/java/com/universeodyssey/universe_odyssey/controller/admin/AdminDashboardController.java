package com.universeodyssey.universe_odyssey.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @GetMapping("/admin/home")
    public String dashboard() {
        return "admin/dashboard";
    }
}
