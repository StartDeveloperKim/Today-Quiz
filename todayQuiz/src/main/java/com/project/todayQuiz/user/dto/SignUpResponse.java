package com.project.todayQuiz.user.dto;

import lombok.Getter;

@Getter
public class SignUpResponse {

    private final Boolean isSignUp;
    private final String message;

    public SignUpResponse(Boolean isSignUp) {
        this.isSignUp = isSignUp;
        this.message = isSignUp.equals(Boolean.TRUE) ? "회원가입에 성공했습니다." : "잘못된 요청입니다.";
    }
}
