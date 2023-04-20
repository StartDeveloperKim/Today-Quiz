package com.project.todayQuiz.home;

import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Controller
public class IndexController {

    private final RefreshTokenDao refreshTokenDao;

    @GetMapping("/")
    public String home(Model model) {
        AuthInfo authInfo = (AuthInfo) model.asMap().get("authInfo");
        if (authInfo != null) {
            refreshTokenDao.saveRefreshToken(authInfo.getRefreshToken(), authInfo.getEmail(), Duration.ofDays(1));
            model.addAttribute("authInfo", authInfo);
        }else{
            log.error("securityToken not exist");
        }
        return "index";
    }
}
