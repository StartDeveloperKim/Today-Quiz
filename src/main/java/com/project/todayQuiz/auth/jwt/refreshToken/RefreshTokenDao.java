package com.project.todayQuiz.auth.jwt.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RefreshTokenDao {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String refreshToken, String email, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(refreshToken, email, duration);
    }

    public String getEmail(String refreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(refreshToken);
    }

    public void deleteValues(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }


}
