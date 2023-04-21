package com.project.todayQuiz.auth.jwt.dto;

import com.nimbusds.oauth2.sdk.token.Tokens;
import lombok.*;

@NoArgsConstructor
@ToString
@Setter
@Getter
public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
