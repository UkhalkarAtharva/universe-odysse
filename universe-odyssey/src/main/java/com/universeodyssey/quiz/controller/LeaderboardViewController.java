package com.universeodyssey.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardViewController {

    @GetMapping("/leaderboard")
    public String leaderboardPage() {
        return "leaderboard";
    }
}
