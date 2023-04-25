package com.project.todayQuiz.quiz.service;

import com.project.todayQuiz.quiz.dto.request.QuizRequest;
import com.project.todayQuiz.quiz.dto.request.QuizUpdateRequest;
import com.project.todayQuiz.quiz.dto.response.AdminQuizResponse;
import com.project.todayQuiz.quiz.dto.response.TodayQuizResponse;

import java.time.LocalDate;
import java.util.List;

public interface QuizService {

    Long postQuiz(QuizRequest quizRequest);

    TodayQuizResponse getTodayQuiz(LocalDate quizDate);

    List<AdminQuizResponse> getQuizList(int page, int size);

    void updateQuiz(QuizUpdateRequest quizUpdateRequest);

    Long countQuiz();

    void removeQuiz(Long id);
}
