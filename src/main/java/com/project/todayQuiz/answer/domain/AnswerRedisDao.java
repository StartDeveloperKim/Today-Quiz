package com.project.todayQuiz.answer.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class AnswerRedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ANSWER_KEY = "answer";

    public void saveAnswer(String answer, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(ANSWER_KEY, answer, duration);
    }

    public String getAnswer() {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(ANSWER_KEY);
    }

}
