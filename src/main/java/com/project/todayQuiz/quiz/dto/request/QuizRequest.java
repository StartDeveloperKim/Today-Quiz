package com.project.todayQuiz.quiz.dto.request;

import lombok.*;

@ToString
@NoArgsConstructor
@Getter
public class QuizRequest {
    private String question;
    private String answer;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Builder
    public QuizRequest(String question, String answer, int year, int month, int day, int hour, int minute) {
        this.question = question;
        this.answer = answer;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
}
