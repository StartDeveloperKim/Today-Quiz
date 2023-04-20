package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {

    private final TokenProvider tokenProvider;
    private final SecurityTokenDao securityTokenDao;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @PostMapping("/api/reissue")
    @ResponseBody
    public ResponseEntity<TokenResponse> reissueAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                            HttpServletResponse response) {
        if (refreshToken != null) {
            TokenResponse tokenResponse = tokenProvider.reissueAccessToken(refreshToken);
            Cookie refreshTokenCookie = CookieUtil.getRefreshTokenCookie(tokenResponse.getRefreshToken());

            response.addCookie(refreshTokenCookie);
            return ResponseEntity.ok(tokenResponse);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/auth")
    public String authRedirect(@RequestParam(name = "token") String securityToken,
                               RedirectAttributes attributes) {
        AuthInfo authInfo = securityTokenDao.getTokenInfo(securityToken);
        if (authInfo ==null) {
            return "redirect:/error";
        }
        log.info("/auth, securityToken: {}", securityToken);
        log.info("/auth, accessToken: {}", authInfo.getAccessToken());
        log.info("/auth, refreshToken: {}", authInfo.getRefreshToken());

        attributes.addFlashAttribute("authInfo", authInfo);

        return "redirect:/";
    }
}
