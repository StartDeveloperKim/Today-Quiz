package com.project.todayQuiz.auth.exception;

public class JWTCookieExpireException extends RuntimeException{
    private final String errorMessage = "엑세스토큰 쿠키가 만료되었습니다.";

    public JWTCookieExpireException() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
