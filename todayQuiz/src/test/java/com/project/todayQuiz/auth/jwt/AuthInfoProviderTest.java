package com.project.todayQuiz.auth.jwt;

import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthInfoProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    private final String NICKNAME = "nickname";
    private final String EMAIL = "email";

    @Test
    void AccessToekn을_발급하고_검증하면_통과된다() {
        //given
        String accessToken = tokenProvider.createAccessToken(EMAIL, NICKNAME);
        //when
        boolean result = tokenProvider.validateToken(accessToken, TokenType.ACCESS);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void RefreshToken을_발급하고_검증하면_통과된다() {
        //given
        String refreshToken = tokenProvider.createRefreshToken(EMAIL, NICKNAME);
        //when
        boolean result = tokenProvider.validateToken(refreshToken, TokenType.REFRESH);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void Token의_Claim을_정상적으로_파싱한다() {
        //given
        String accessToken = tokenProvider.createAccessToken(EMAIL, NICKNAME);
        //when
        UserInfo userInfo = tokenProvider.getTokenInfo(accessToken, TokenType.ACCESS);
        //then
        assertThat(userInfo.getEmail()).isEqualTo(EMAIL);
        assertThat(userInfo.getNickname()).isEqualTo(NICKNAME);
    }
}