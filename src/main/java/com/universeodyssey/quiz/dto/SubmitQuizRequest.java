package com.universeodyssey.quiz.dto;

import java.util.Map;

public class SubmitQuizRequest {
    public Long quizId;
    public Map<Long, Integer> answers; // questionId -> selectedIndex
}
