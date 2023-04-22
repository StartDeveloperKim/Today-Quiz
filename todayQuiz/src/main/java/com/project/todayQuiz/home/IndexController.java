package com.project.todayQuiz.home;

import com.project.todayQuiz.auth.LoginUser;
import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Controller
public class IndexController {

    private final RefreshTokenDao refreshTokenDao;

    @GetMapping("/")
    public String home(@LoginUser UserInfo userInfo, Model model, HttpServletResponse response) {
        AuthInfo authInfo = (AuthInfo) model.asMap().get("authInfo");
        if (authInfo != null) {
            refreshTokenDao.saveRefreshToken(authInfo.getRefreshToken(), authInfo.getEmail(), Duration.ofDays(1));
            CookieUtil.addTokenCookie(response, authInfo.getAccessToken(), authInfo.getRefreshToken());
            model.addAttribute("userInfo", new UserInfo(authInfo.getEmail(), authInfo.getNickname(), authInfo.getRole()));
        }else{
            log.info("securityToken not exist");
        }

        if (userInfo != null){
            log.info("userInfo : {}", userInfo.toString());
            model.addAttribute("userInfo", userInfo);
        }
        return "index";
    }
}
