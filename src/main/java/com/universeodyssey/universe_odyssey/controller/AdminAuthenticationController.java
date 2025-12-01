package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.AdminUser;
import com.universeodyssey.universe_odyssey.repository.AdminUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthenticationController {

    private final AdminUserRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(AdminAuthenticationController.class);

    public AdminAuthenticationController(AdminUserRepository adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest req) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing credentials"));
        }

        AdminUser admin = adminRepo.findByUsername(username);
        if (admin == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid username or password"));
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("adminId", admin.getId());
        // set Spring security context for servlet filters if needed
        session.setAttribute("SPRING_SECURITY_CONTEXT", org.springframework.security.core.context.SecurityContextHolder.getContext());

        log.info("Admin logged in: {}", username);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Login successful");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok(Map.of("success", true));
    }
}
