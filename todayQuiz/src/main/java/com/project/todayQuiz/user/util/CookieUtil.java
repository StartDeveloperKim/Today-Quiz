package com.project.todayQuiz.user.util;

import javax.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie getRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        int maxAge = 60 * 60 * 24;

        cookie.setHttpOnly(true);
        cookie.setPath("/api/reissue");
        cookie.setMaxAge(maxAge);

        return cookie;
    }
}
