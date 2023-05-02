package com.project.todayQuiz.answer.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnswerRedisDaoTest {


    @Autowired
    private AnswerRedisDao answerRedisDao;

    @Test
    void 정답을_Redis에_저장하고_조회한다() {
        //given
        String answer = "answer";
        //when
        answerRedisDao.saveAnswer(answer, Duration.ofSeconds(30));
        String savedAnswer = answerRedisDao.getAnswer();
        //then
        assertThat(answer).isEqualTo(savedAnswer);
    }
}