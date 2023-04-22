package com.project.todayQuiz.auth.securityToken;

import com.project.todayQuiz.user.domain.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@ToString
@Getter
public class AuthInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private String email;
    private String nickname;
    private Role role;

    public AuthInfo() {
    }

    public AuthInfo(String accessToken, String refreshToken, String email, String nickname, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
