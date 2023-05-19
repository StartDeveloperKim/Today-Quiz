package com.project.todayQuiz.answer.dto;

import lombok.Getter;

@Getter
public enum AnswerState {
    CORRECT("정답입니다!!!"), WRONG("오답입니다!!"), DUPLICATE("정답 중복입력은 안됩니다!!");

    private final String stateMessage;

    AnswerState(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
