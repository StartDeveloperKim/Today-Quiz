package com.project.todayQuiz.home;

import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.quiz.dto.response.TodayQuizResponse;
import com.project.todayQuiz.quiz.service.QuizService;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
@Controller
public class IndexController {

    private final RefreshTokenDao refreshTokenDao;
    private final QuizService quizService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserInfo userInfo, Model model, HttpServletResponse response) {
        AuthInfo authInfo = (AuthInfo) model.asMap().get("authInfo");
        if (authInfo != null) {
            refreshTokenDao.saveRefreshToken(authInfo.getRefreshToken(), authInfo.getEmail(), Duration.ofDays(1));
            CookieUtil.addTokenCookie(response, authInfo.getAccessToken(), authInfo.getRefreshToken());
            model.addAttribute("userInfo", new UserInfo(authInfo.getEmail(), authInfo.getNickname(), authInfo.getRole()));
        }

        if (userInfo != null){
            model.addAttribute("userInfo", userInfo);
        }

        TodayQuizResponse todayQuiz = quizService.getTodayQuiz(LocalDate.now());
        model.addAttribute("todayQuiz", todayQuiz);

        return "index";
    }
}
