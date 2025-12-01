package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.dto.GeminiChatRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/gemini")
public class GeminiProxyController {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@Valid @RequestBody GeminiChatRequest req) {

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(500).body(Map.of("error", "Gemini API key missing"));
        }

        if (req.getQuestion() == null || req.getQuestion().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing question field"));
        }

        String planet = (req.getPlanet() != null) ? req.getPlanet() : "the planet";

        // SYSTEM + USER PROMPT
        String message =
                "You are Cosmos AI, an intelligent space assistant. " +
                        "Provide accurate and scientific details ONLY about " + planet + ". " +
                        "Be friendly but factual.\n\nUser: " + req.getQuestion();

        // ⭐ CORRECT GEMINI REQUEST FORMAT ⭐
        Map<String, Object> payload = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", message)
                                )
                        )
                )
        );

        String url =
                "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            System.out.println("🤖 Sending to Gemini: " + message);
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("✅ Gemini response: " + response.getBody());
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Gemini error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Gemini request failed", "details", e.getMessage()));
        }
    }
}
