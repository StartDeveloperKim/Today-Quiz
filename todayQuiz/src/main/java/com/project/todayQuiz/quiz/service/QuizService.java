package com.project.todayQuiz.quiz.service;

import com.project.todayQuiz.quiz.dto.request.QuizPostRequest;
import com.project.todayQuiz.quiz.dto.response.AdminQuizResponse;
import com.project.todayQuiz.quiz.dto.response.TodayQuizResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface QuizService {

    Long postQuiz(QuizPostRequest quizPostRequest);

    TodayQuizResponse getTodayQuiz(LocalDate quizDate);

    List<AdminQuizResponse> getQuizList(int page, int size);

    Long countQuiz();

    void removeQuiz(Long id);
}
