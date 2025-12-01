package com.universeodyssey.quiz.controller;

import com.universeodyssey.quiz.dto.QuizDto;
import com.universeodyssey.quiz.dto.SubmitQuizRequest;
import com.universeodyssey.quiz.dto.SubmitQuizResponse;
import com.universeodyssey.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayQuiz(Authentication auth) {
        if (auth == null)
            return ResponseEntity.status(401).build();
        String email = auth.getName();
        return quizService.getTodayQuizForUser(email).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable Long quizId, @RequestBody SubmitQuizRequest request,
            Authentication auth) {
        if (auth == null)
            return ResponseEntity.status(401).build();
        String email = auth.getName();
        if (!quizId.equals(request.quizId))
            return ResponseEntity.badRequest().body("Quiz id mismatch");
        SubmitQuizResponse resp = quizService.submitAnswers(email, request);
        if (resp.message.equals("Already submitted"))
            return ResponseEntity.status(409).body(resp);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/admin/regenerate")
    public ResponseEntity<?> regenerateTodayQuiz(Authentication auth) {
        if (auth == null)
            return ResponseEntity.status(401).build();

        // Check if user has ADMIN role
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        quizService.regenerateTodayQuiz();
        return ResponseEntity.ok().body("Quiz regenerated successfully");
    }
}
