package com.project.todayQuiz.quiz.controller;

import com.project.todayQuiz.quiz.controller.QuizController;
import com.project.todayQuiz.quiz.dto.page.Paging;
import com.project.todayQuiz.quiz.dto.response.AdminQuizResponse;
import com.project.todayQuiz.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller("/admin")
public class AdminController {

    private final QuizService quizService;

    @GetMapping("/{page}")
    public String adminPage(@PathVariable("page") int page,
                            Model model) {
        int size = 10;
        Paging pagingInfo = Paging.of(page, quizService.countQuiz());
        List<AdminQuizResponse> quizList = quizService.getQuizList(page, size);

        model.addAttribute("quizzes", quizList);
        model.addAttribute("paging", pagingInfo);
        return "admin";
    }

}
