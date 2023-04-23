package com.project.todayQuiz.quiz.dto.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseData<T> {

    private final T state;
    private final String error;
}
