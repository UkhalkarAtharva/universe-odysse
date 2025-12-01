package com.universeodyssey.quiz.controller;

import com.universeodyssey.quiz.repository.UserPointsRepository;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    private final UserPointsRepository userPointsRepository;
    private final UserRepository userRepository;

    public LeaderboardController(UserPointsRepository userPointsRepository, UserRepository userRepository) {
        this.userPointsRepository = userPointsRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> leaderboard(Authentication auth) {
        List<?> list = getTopUsers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/tiers")
    public ResponseEntity<?> tiers() {
        // simple static tiers; in a full implementation we would read the DB
        List<Map<String,Object>> tiers = new ArrayList<>();
        tiers.add(Map.of("name","Radiant","capacity",10));
        tiers.add(Map.of("name","Immortal","capacity",25));
        tiers.add(Map.of("name","Guardian","capacity",50));
        tiers.add(Map.of("name","Participant","capacity",999999));
        return ResponseEntity.ok(tiers);
    }

    private List<Map<String,Object>> getTopUsers() {
        List<Map<String,Object>> out = new ArrayList<>();
        var top = userPointsRepository.findTop100ByOrderByTotalPointsDesc();
        int rank = 1;
        for (var up : top) {
            Map<String,Object> item = new HashMap<>();
            item.put("userId", up.getUserId());
            User u = userRepository.findById(up.getUserId()).orElse(null);
            item.put("username", u == null ? "Unknown" : u.getUsername());
            item.put("totalPoints", up.getTotalPoints());
            item.put("rank", rank++);
            out.add(item);
        }
        return out;
    }
}
