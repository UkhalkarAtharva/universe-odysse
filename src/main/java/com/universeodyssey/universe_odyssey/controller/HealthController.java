package com.universeodyssey.universe_odyssey.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @GetMapping("/")
    public String home() {
        return "OK";
    }
}
