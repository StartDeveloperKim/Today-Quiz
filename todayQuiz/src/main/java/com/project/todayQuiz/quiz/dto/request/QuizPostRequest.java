package com.project.todayQuiz.quiz.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QuizPostRequest {
    private final String question;
    private final String answer;
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;

}
