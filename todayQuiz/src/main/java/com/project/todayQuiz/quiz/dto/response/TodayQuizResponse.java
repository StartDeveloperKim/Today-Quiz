package com.project.todayQuiz.quiz.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TodayQuizResponse {

    private final String question;
    private final String postDate;

    public TodayQuizResponse(String question, LocalDateTime postDate) {
        this.question = question;
        this.postDate = postDate.format(DateTimeFormatter.ISO_DATE);
    }
}
