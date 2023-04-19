package com.project.todayQuiz.auth.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class TokenResponse {

    private final String accessToken;
    private final String refreshToken;

}
