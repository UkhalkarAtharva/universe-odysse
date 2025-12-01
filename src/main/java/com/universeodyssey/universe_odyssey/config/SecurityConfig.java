package com.universeodyssey.universe_odyssey.config;

import com.universeodyssey.universe_odyssey.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth
                        // Static assets and frontend SPA
                        .requestMatchers(
                                "/",
                                "/health",
                                "/index.html",
                                "/login.html",
                                "/signup.html",
                                "/css/**",
                                "/js/**",
                                "/assets/**",
                                "/videos/**",
                                "/animations/**",
                                "/favicon.png")
                        .permitAll()

                        // Frontend SPA routes (forward to React)
                        .requestMatchers(
                                "/home",
                                "/planets",
                                "/missions",
                                "/facts",
                                "/about",
                                "/explore",
                                "/news",
                                "/news/**",
                                "/blogs",
                                "/blogs/**",
                                "/planet/**")
                        .permitAll()

                        // Quiz & Leaderboard endpoints - allow public access (auth checked in
                        // view/controller)
                        .requestMatchers("/api/quiz/**", "/api/leaderboard/**").permitAll()
                        .requestMatchers("/quiz", "/leaderboard").permitAll()
                        // Other public API endpoints
                        .requestMatchers("/api/**").permitAll()

                        // User authentication routes
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/signup/**", "/login/**").permitAll()

                        // Admin login (before authentication required)
                        .requestMatchers("/admin/login", "/admin/auth/**").permitAll()

                        // Admin area - require ADMIN role
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // User profile - require authentication
                        .requestMatchers("/profile/**", "/profile", "/user/**").authenticated()

                        // Everything else allowed
                        .anyRequest().permitAll())

                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/process-login")
                        .successHandler(new com.universeodyssey.universe_odyssey.security.AdminAuthSuccessHandler())
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
