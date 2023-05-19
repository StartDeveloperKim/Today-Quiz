package com.project.todayQuiz.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String NICKNAME = "nickname";
    private final String EMAIL = "email";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .nickname(NICKNAME)
                .authProvider("google")
                .role(Role.GUEST)
                .email(EMAIL)
                .picture("picture").build();
        userRepository.save(user);
    }

    @AfterEach
    void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    void 파라미터로_전달된_닉네임이_존재하면_True반환() {
        //when
        Boolean result = userRepository.existsByNickname(NICKNAME);
        //then
        assertThat(result).isTrue();
    }

}