package com.project.todayQuiz.answer.service;

import com.project.todayQuiz.answer.dto.AnswerResponse;
import com.project.todayQuiz.answer.dto.AnswerState;

import java.time.LocalDateTime;

public interface AnswerService {

    AnswerResponse checkAnswer(String submitAnswer, LocalDateTime todayDate, String email);

}
