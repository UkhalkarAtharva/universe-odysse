package com.universeodyssey.universe_odyssey.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AdminAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // set admin name in session
        String name = authentication.getName();
        HttpSession session = request.getSession(true);
        session.setAttribute("adminName", name);
        log.info("Admin authenticated: {}", name);
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
