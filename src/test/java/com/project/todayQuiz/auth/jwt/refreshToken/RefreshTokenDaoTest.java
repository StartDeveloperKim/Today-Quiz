package com.project.todayQuiz.auth.jwt.refreshToken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
class RefreshTokenDaoTest {

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Test
    void redis에_refreshToken이_저장되고_조회된다() {
        //given
        String REFRESH_TOKEN = "refreshToken";
        String EMAIL = "email";
        //when
        refreshTokenDao.saveRefreshToken(REFRESH_TOKEN, EMAIL, Duration.ofDays(1));
        String email = refreshTokenDao.getEmail(REFRESH_TOKEN);
        //then
        assertThat(email).isEqualTo(EMAIL);
    }

}