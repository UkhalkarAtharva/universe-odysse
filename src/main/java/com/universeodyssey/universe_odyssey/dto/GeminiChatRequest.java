package com.universeodyssey.universe_odyssey.dto;

import jakarta.validation.constraints.NotBlank;

public class GeminiChatRequest {

    @NotBlank(message = "question is required")
    private String question;

    // optional, server will use it to build system prompt
    private String planet;

    public GeminiChatRequest() {}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }
}
