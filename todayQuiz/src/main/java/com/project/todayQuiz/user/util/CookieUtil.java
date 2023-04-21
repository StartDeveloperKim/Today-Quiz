package com.project.todayQuiz.user.util;

import com.project.todayQuiz.auth.jwt.dto.TokenResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    public static void addTokenCookie (HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie refreshTokenCookie = CookieUtil.getRefreshTokenCookie(refreshToken, CookieType.NEW, "/api/reissue");
        Cookie logoutRefreshTokenCookie = CookieUtil.getRefreshTokenCookie(refreshToken, CookieType.NEW, "/api/logout");
        Cookie accessTokenCookie = CookieUtil.getAccessTokenCookie(accessToken, CookieType.NEW);
        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);
        response.addCookie(logoutRefreshTokenCookie);
    }

    public static Cookie getRefreshTokenCookie(String refreshToken, CookieType cookieType, String url) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        int maxAge = cookieType.equals(CookieType.NEW) ? 60 * 60 * 24 : 0;

        cookie.setHttpOnly(true);
        cookie.setPath(url);
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public static Cookie getAccessTokenCookie(String accessToken, CookieType cookieType) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
        int maxAge = cookieType.equals(CookieType.NEW) ? 30 * 24 : 0;

        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public static TokenResponse parseToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        TokenResponse tokenResponse = new TokenResponse();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if(name.equals(ACCESS_TOKEN)){
                tokenResponse.setAccessToken(cookie.getValue());
            }
            if (name.equals(REFRESH_TOKEN)) {
                tokenResponse.setRefreshToken(cookie.getValue());
            }
        }
        return tokenResponse;
    }
}
