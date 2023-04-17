package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "redirect: /";
    }

    @PostMapping("/api/reissue")
    @ResponseBody
    public ResponseEntity<TokenResponse> reissueAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                               HttpServletResponse response) {
        if (refreshToken != null) {
            TokenResponse tokenResponse = tokenProvider.reissueAccessToken(refreshToken);
            Cookie refreshTokenCookie = getRefreshTokenCookie(tokenResponse.getRefreshToken());

            response.addCookie(refreshTokenCookie);
            return ResponseEntity.ok(tokenResponse);
        }
        return ResponseEntity.badRequest().body(null);
    }

    private Cookie getRefreshTokenCookie(String refreshToken) {
        Instant instant = Instant.now().plus(Duration.ofHours(23));
        Date expiry = Date.from(instant);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/reissue");
        cookie.setMaxAge((int) Duration.between(Instant.now(), instant).getSeconds());

        return cookie;
    }
}
