package com.project.todayQuiz.auth.oauth;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenDao refreshTokenDao;

    private static final String ERROR_REDIRECT_URL = "http://localhost:8080/error";
    private static final String REDIRECT_URL = "http://localhost:8080/auth?accessToken=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            log.info("OAuth2 Success Handler running...");

            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = customOAuth2User.getAttributes();

            String email = (String) attributes.get("email");
            String nickname = customOAuth2User.getNickname();

            String accessToken = tokenProvider.createAccessToken(email, nickname);
            response.setContentType("application/json");
            response.getWriter().write("{\"accessToken\":\"" + accessToken + "\"}");

            String refreshToken = tokenProvider.createRefreshToken(email, nickname);
            refreshTokenDao.saveRefreshToken(refreshToken, email, Duration.ofDays(1));

            log.info("refreshToken : {}", refreshToken);

            Cookie refreshTokenCookie = CookieUtil.getRefreshTokenCookie(refreshToken);
            response.addCookie(refreshTokenCookie);

            String authRedirectURL = REDIRECT_URL + accessToken + "&refreshToken=" + refreshToken;

            response.sendRedirect(authRedirectURL);
        }else {
            response.sendRedirect(ERROR_REDIRECT_URL); // 에러
        }
    }
}
