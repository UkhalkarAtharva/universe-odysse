package com.universeodyssey.universe_odyssey.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin/login";
    }
}
