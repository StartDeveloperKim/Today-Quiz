package com.project.todayQuiz.auth.securityToken;

import com.project.todayQuiz.user.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SecurityTokenDaoTest {

    @Autowired
    private SecurityTokenDao securityTokenDao;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @Test
    void 보안토큰을_key로_사용하여_TokenInfo를_저장한다() {
        //given
        AuthInfo authInfo = new AuthInfo(ACCESS_TOKEN, REFRESH_TOKEN, "email", "nickname", Role.GUEST);
        String securityToken = SecurityTokenGenerator.generateSecurityToken();
        System.out.println("securityToken = " + securityToken);

        //when
        securityTokenDao.saveTokenInfo(securityToken, authInfo, Duration.ofSeconds(60));

        //then
        AuthInfo savedAuthInfo = securityTokenDao.getTokenInfo(securityToken);

        assertThat(authInfo.getAccessToken()).isEqualTo(savedAuthInfo.getAccessToken());
        assertThat(authInfo.getRefreshToken()).isEqualTo(savedAuthInfo.getRefreshToken());
    }
}