package com.project.todayQuiz.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NicknameCheckRequest {
    private String nickname;

    public NicknameCheckRequest(String nickname) {
        this.nickname = nickname;
    }
}
