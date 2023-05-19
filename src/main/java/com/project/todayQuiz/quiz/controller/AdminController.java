package com.project.todayQuiz.quiz.controller;

import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.quiz.controller.QuizController;
import com.project.todayQuiz.quiz.dto.page.Paging;
import com.project.todayQuiz.quiz.dto.response.AdminQuizResponse;
import com.project.todayQuiz.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuizService quizService;

    @GetMapping("/{page}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminPage(@PathVariable(value = "page") int page,
                            @AuthenticationPrincipal UserInfo userInfo,
                            Model model) {
        int size = 10;
        Paging pagingInfo = Paging.of(page, quizService.countQuiz());
        List<AdminQuizResponse> quizList = quizService.getQuizList(page, size);

        log.info("userInfo : {}", userInfo.toString());

        model.addAttribute("quizzes", quizList);
        model.addAttribute("paging", pagingInfo);
        return "admin";
    }

}
