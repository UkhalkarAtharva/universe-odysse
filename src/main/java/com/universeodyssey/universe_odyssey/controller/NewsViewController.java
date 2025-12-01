package com.universeodyssey.universe_odyssey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NewsViewController {

    // Forward /news route to standalone news page
    @GetMapping("/news")
    public String newsPage() {
        return "forward:/news.html";
    }

    @GetMapping("/news/{id}")
    public String newsDetailPage(@PathVariable Long id) {
        return "forward:/news-detail.html?id=" + id;
    }
}
