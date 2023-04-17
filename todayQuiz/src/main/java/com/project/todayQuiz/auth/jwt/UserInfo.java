package com.project.todayQuiz.auth.jwt;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserInfo {
    private final String email;
    private final String nickname;

    public UserInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
