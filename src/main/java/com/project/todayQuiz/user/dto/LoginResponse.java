package com.project.todayQuiz.user.dto;

import com.project.todayQuiz.auth.securityToken.SecurityTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class LoginResponse {

    private Boolean isLogin;
    private String message;
    private String securityToken;

    public LoginResponse(Boolean isLogin, String securityToken) {
        this.isLogin = isLogin;
        this.message = isLogin.equals(Boolean.TRUE) ? "로그인에 성공했습니다." : "아이디 또는 비밀번호가 틀렸습니다.";
        this.securityToken = securityToken;
    }
}
