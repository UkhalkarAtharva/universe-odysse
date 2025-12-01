package com.universeodyssey.quiz.dto;

import java.time.LocalDate;
import java.util.List;

public class QuizDto {
    public Long id;
    public String title;
    public LocalDate date;
    public List<QuizQuestionDto> questions;
    public boolean completed;
    public Integer previousScore;
}
