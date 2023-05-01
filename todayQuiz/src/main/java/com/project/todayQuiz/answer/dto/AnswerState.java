package com.project.todayQuiz.answer.dto;

import lombok.Getter;

@Getter
public enum AnswerState {
    CORRECT("정답입니다!!!"), WRONG("오답입니다!!");

    private final String stateMessage;

    AnswerState(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
