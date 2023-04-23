package com.project.todayQuiz.quiz.dto.response;

import com.project.todayQuiz.quiz.domain.Quiz;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class AdminQuizResponse {
    private final Long id;
    private final String question;
    private final String answer;
    private final String postDate;

    public AdminQuizResponse(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
        this.postDate = quiz.getPostDate().format(DateTimeFormatter.ISO_DATE);
    }
}
