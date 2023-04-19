package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {

    private final TokenProvider tokenProvider;

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
    public String authRedirect(@RequestParam(name = "accessToken") String accessToken,
                               @RequestParam(name = "refreshToken") String refreshToken,
                               RedirectAttributes attributes) {
        log.info("/auth, accessToken: {}", accessToken);
        log.info("/auth, refreshToken: {}", refreshToken);

        attributes.addFlashAttribute("tokenResponse", new TokenResponse(accessToken, refreshToken));

        return "redirect:/";
    }
}
