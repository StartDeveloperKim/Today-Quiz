package com.project.todayQuiz.quiz.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseData<T> {

    private final T state;
    private final String error;
}
