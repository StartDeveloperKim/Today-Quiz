package com.project.todayQuiz.quiz.dto.response;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TodayQuizResponse {

    private final String question;
    private final String postDate;
    private final Boolean isPost;

    public TodayQuizResponse(String question, LocalDateTime postDate) {
        long seconds = Duration.between(LocalDateTime.now(), postDate).getSeconds();
        this.question = question;
        this.postDate = postDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.isPost = seconds > 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    public TodayQuizResponse(String question, String postDate) {
        this.question = question;
        this.postDate = postDate;
        this.isPost = Boolean.FALSE;
    }
}
