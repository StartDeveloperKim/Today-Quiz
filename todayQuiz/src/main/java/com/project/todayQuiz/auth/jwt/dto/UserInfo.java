package com.project.todayQuiz.auth.jwt.dto;

import com.project.todayQuiz.user.domain.Role;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserInfo {
    private final String email;
    private final String nickname;
    private final Role role;

    public UserInfo(String email, String nickname, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
