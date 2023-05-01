package com.project.todayQuiz.answer.service;

import java.time.LocalDateTime;

public interface AnswerService {

    Boolean checkAnswer(String submitAnswer, LocalDateTime todayDate, String email);
}
