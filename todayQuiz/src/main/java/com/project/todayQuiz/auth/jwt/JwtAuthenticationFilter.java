package com.project.todayQuiz.auth.jwt;

import com.project.todayQuiz.auth.exception.JWTCookieExpireException;
import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.user.util.CookieType;
import com.project.todayQuiz.user.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RefreshTokenDao refreshTokenDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TokenResponse tokenResponse = CookieUtil.parseToken(request);
        log.info("tokenResponse : {}", tokenResponse.toString());
        try {
            log.info("JWT Filter running... {}", request.getRequestURI());

            String accessToken = tokenResponse.getAccessToken();
            if (accessToken == null) {
                throw new JWTCookieExpireException();
            }

            if (tokenProvider.validateToken(accessToken, TokenType.ACCESS)) {
                log.info("AccessToken Info : {}", accessToken);
                saveUserInfo(request, accessToken);
            }
        } catch (JWTCookieExpireException | ExpiredJwtException e) {
            log.error("Expired AccessToken");
            String refreshToken = tokenResponse.getRefreshToken();
            if (refreshToken!= null && tokenProvider.validateToken(refreshToken, TokenType.REFRESH)) {
                TokenResponse newTokenResponse = tokenProvider.reissueAccessToken(refreshToken);
                if (newTokenResponse == null) {
                    response.sendRedirect("/api/logout");
                }else {
                    CookieUtil.addTokenCookie(response, newTokenResponse.getAccessToken(), newTokenResponse.getRefreshToken());
                    saveUserInfo(request, newTokenResponse.getAccessToken());
                }
            }else {
                response.sendRedirect("/api/logout");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void saveUserInfo(HttpServletRequest request, String accessToken) {
        UserInfo userinfo = tokenProvider.getTokenInfo(accessToken, TokenType.ACCESS);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userinfo, null, AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}
