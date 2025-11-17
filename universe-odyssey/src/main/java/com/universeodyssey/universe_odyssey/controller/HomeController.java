package com.universeodyssey.universe_odyssey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/home")
    public String homePath() {
        return "forward:/index.html";
    }

    @GetMapping("/planets")
    public String planets() {
        return "forward:/index.html";
    }

    @GetMapping("/missions")
    public String missions() {
        return "forward:/index.html";
    }

    @GetMapping("/planet-detail")
    public String planetDetail() {
        return "forward:/planet-detail.html";
    }

    @GetMapping("/facts")
    public String facts() {
        return "forward:/index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "forward:/index.html";
    }

    @GetMapping("/explore")
    public String explore() {
        return "forward:/index.html";
    }

    // Planet detail route (previous SPA route) removed to ensure server handles planet flow
    // (React route /planet/:name should be removed from the client router)

    // Chat route removed from HomeController; ChatController handles /chat


}
