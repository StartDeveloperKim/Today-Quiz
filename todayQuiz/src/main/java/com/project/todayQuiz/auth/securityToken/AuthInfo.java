package com.project.todayQuiz.auth.securityToken;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AuthInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private String email;

    public AuthInfo() {
    }

    public AuthInfo(String accessToken, String refreshToken, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
    }
}
