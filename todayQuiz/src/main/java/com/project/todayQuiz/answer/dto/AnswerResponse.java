package com.project.todayQuiz.answer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class AnswerResponse {
    private final String state;
    private final String message;
    private final Integer todayRank;

    public AnswerResponse(String state, String message, Integer todayRank) {
        this.state = state;
        this.message = message;
        this.todayRank = todayRank;
    }
}
