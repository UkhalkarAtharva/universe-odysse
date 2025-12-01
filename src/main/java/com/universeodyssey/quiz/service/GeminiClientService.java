package com.universeodyssey.quiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class GeminiClientService {
    private static final Logger log = LoggerFactory.getLogger(GeminiClientService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;
    private final String apiUrl;

    public GeminiClientService(@Value("${GEMINI_API_KEY:}") String apiKey,
            @Value("${GEMINI_API_URL:https://api.gemini-placeholder.example/v1}") String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    /**
     * Generate a quiz from Gemini (raw JSON string)
     */
    public Optional<String> generateQuiz(LocalDate date) {
        try {
            String prompt = String.format(
                    "Generate a challenging and engaging 'Daily Space Quiz' for %s. " +
                            "Topics: Astronomy, Space Missions, Astrophysics, Planets, Black Holes, Cosmology. " +
                            "Create exactly 5 questions. " +
                            "Output ONLY valid JSON in this EXACT format (no markdown, no code blocks): " +
                            "{\"quiz\":{\"questions\":[{\"question\":\"...\",\"options\":[\"A\",\"B\",\"C\",\"D\"],\"correct_index\":0,\"points\":10}]}}",
                    date.toString());

            // Build proper Gemini API request body
            JsonNode requestBody = mapper.createObjectNode()
                    .set("contents", mapper.createArrayNode()
                            .add(mapper.createObjectNode()
                                    .set("parts", mapper.createArrayNode()
                                            .add(mapper.createObjectNode()
                                                    .put("text", prompt)))));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Gemini API uses API key as query parameter, not Bearer token
            String urlWithKey = apiUrl + "?key=" + (apiKey != null ? apiKey : "");

            HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(urlWithKey, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

                if (responseBody != null) {
                    // Parse Gemini API response format
                    JsonNode root = mapper.readTree(responseBody);
                    JsonNode candidates = root.path("candidates");
                    if (candidates.isArray() && candidates.size() > 0) {
                        JsonNode content = candidates.get(0).path("content");
                        JsonNode parts = content.path("parts");
                        if (parts.isArray() && parts.size() > 0) {
                            String text = parts.get(0).path("text").asText();
                            // Clean markdown code blocks if present
                            text = text.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
                            log.info("Successfully generated quiz from Gemini API");
                            return Optional.of(text);
                        }
                    }
                }
                throw new RuntimeException("Gemini response parsing failed. Body: " + responseBody);
            }
            throw new RuntimeException(
                    "Gemini returned non-2xx: " + response.getStatusCodeValue() + " Body: " + response.getBody());
        } catch (Exception ex) {
            log.error("Gemini call failed: {}", ex.getMessage(), ex);
            // We rethrow runtime exceptions to be caught by caller if they want detailed
            // error
            throw new RuntimeException("Gemini call failed: " + ex.getMessage(), ex);
        }
    }

    public Optional<JsonNode> generateStructuredQuiz(LocalDate date) {
        try {
            Optional<String> raw = generateQuiz(date);
            if (raw.isEmpty())
                return Optional.empty();
            return Optional.ofNullable(mapper.readTree(raw.get()));
        } catch (Exception ex) {
            log.error("Failed to parse Gemini response: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
