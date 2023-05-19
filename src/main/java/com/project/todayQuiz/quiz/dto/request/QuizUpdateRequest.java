package com.project.todayQuiz.quiz.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QuizUpdateRequest extends QuizRequest{
    private Long id;

    public QuizUpdateRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
