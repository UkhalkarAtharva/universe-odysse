package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final NewsService newsService;

    public HomeController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Root "/" is now handled by HealthController

    @GetMapping("/home")
    public String homePath(Model model) {
        model.addAttribute("latestNews", newsService.getLatestNews(5));
        return "home";
    }

    @GetMapping("/planets")
    public String planets() {
        return "forward:/index.html";
    }

    @GetMapping("/missions")
    public String missions() {
        return "forward:/missions.html";
    }

    // Quiz and leaderboard routes are forwarded by SpaForwardingController

    @GetMapping("/planet-detail")
    public String planetDetail() {
        return "forward:/planet-detail.html";
    }

    @GetMapping("/facts")
    public String facts() {
        return "forward:/index.html";
    }

    @GetMapping("/blogs/new")
    public String newBlog() {
        return "forward:/blog-editor.html";
    }

    @GetMapping("/blogs/{id}")
    public String blogDetail(@PathVariable String id) {
        return "forward:/blog-detail.html";
    }

    @GetMapping("/blogs/{id}/edit")
    public String editBlog() {
        return "forward:/blog-editor.html";
    }
    // Chat route removed from HomeController; ChatController handles /chat

}
