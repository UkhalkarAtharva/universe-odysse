package com.universeodyssey.universe_odyssey.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.universeodyssey.universe_odyssey.repository.PlanetDetailRepository;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import com.universeodyssey.universe_odyssey.repository.BlogRepository;
import org.springframework.ui.Model;

@Controller
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final PlanetDetailRepository planetRepository;
    private final BlogRepository blogRepository;

    public AdminDashboardController(UserRepository userRepository, PlanetDetailRepository planetRepository,
            BlogRepository blogRepository) {
        this.userRepository = userRepository;
        this.planetRepository = planetRepository;
        this.blogRepository = blogRepository;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("planetCount", planetRepository.count());
        model.addAttribute("blogCount", blogRepository.count());
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/admin/news")
    public String news(Model model) {
        model.addAttribute("activePage", "news");
        return "admin/news-list";
    }

    @GetMapping("/admin/comments")
    public String comments(Model model) {
        model.addAttribute("activePage", "comments");
        return "admin/comments-list";
    }

    @GetMapping("/admin/analytics")
    public String analytics(Model model) {
        model.addAttribute("activePage", "analytics");
        return "admin/analytics";
    }

    @GetMapping("/admin/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        return "admin/settings";
    }
}
