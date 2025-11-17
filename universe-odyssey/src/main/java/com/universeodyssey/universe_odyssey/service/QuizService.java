package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.QuizQuestion;
import com.universeodyssey.universe_odyssey.repository.QuizQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuizService {

    private final QuizQuestionRepository questionRepository;

    public QuizService(QuizQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuizQuestion> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<QuizQuestion> generateQuiz(int limit) {
        List<QuizQuestion> questions = questionRepository.findAll();
        Collections.shuffle(questions);
        return questions.stream().limit(limit).toList();
    }

    public int calculateScore(List<Long> questionIds, List<Long> selectedOptionIds) {
        int score = 0;

        for (int i = 0; i < questionIds.size(); i++) {
            QuizQuestion q = questionRepository.findById(questionIds.get(i)).orElse(null);
            if (q != null && q.getCorrectAnswer() != null && q.getCorrectAnswer().getId().equals(selectedOptionIds.get(i))) {
                score++;
            }
        }

        return score;
    }
}
