package com.universeodyssey.universe_odyssey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // resolves to src/main/resources/templates/chat.html
    }
}
