package com.project.todayQuiz.answer.dto;

import lombok.RequiredArgsConstructor;

public class AnswerResponse {
    private final Boolean isAnswer;
    private final String message;

    public AnswerResponse(Boolean isAnswer, AnswerState answerState) {
        this.isAnswer = isAnswer;
        this.message = answerState.getStateMessage();
    }
}
