package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.dto.LoginRequest;
import com.universeodyssey.universe_odyssey.dto.SignupRequest;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import com.universeodyssey.universe_odyssey.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletRequest httpRequest) {
        try {
            User user = userService.registerUser(request);

            // create session and set security context for simple auth
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("userId", user.getId());

            // Set Spring Security context so protected endpoints work
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), null, java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                // Ensure security context is stored in session for Spring Security filters
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("message", "Signup successful");
            body.put("user", sanitize(user));
            log.info("New user signed up: {}", user.getEmail());
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException ex) {
            log.warn("Signup validation failed: {}", ex.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception ex) {
            log.error("Unexpected error during signup", ex);
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", ex.getMessage() != null ? ex.getMessage() : "An error occurred while signing up");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            User user = userService.authenticateUser(request);

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("userId", user.getId());


                Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), null, java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                // Ensure security context is stored in session for Spring Security filters
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("message", "Login successful");
            body.put("user", sanitize(user));
            log.info("User logged in: {}", user.getEmail());
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException ex) {
            log.warn("Login failed: {}", ex.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception ex) {
            log.error("Unexpected error during login", ex);
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", ex.getMessage() != null ? ex.getMessage() : "An error occurred while logging in");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            return ResponseEntity.ok(body);
        }

        Object uid = session.getAttribute("userId");
        if (uid == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            return ResponseEntity.ok(body);
        }

        Long userId = (uid instanceof Long) ? (Long) uid : Long.parseLong(uid.toString());
        return userRepository.findById(userId).map(user -> {
            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("user", sanitize(user));
            return ResponseEntity.ok(body);
        }).orElseGet(() -> {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            return ResponseEntity.ok(body);
        });
    }

    private Map<String, Object> sanitize(User user) {
        Map<String, Object> u = new HashMap<>();
        u.put("id", user.getId());
        u.put("fullName", user.getFullName());
        u.put("email", user.getEmail());
        u.put("createdAt", user.getCreatedAt());
        u.put("updatedAt", user.getUpdatedAt());
        return u;
    }
}
