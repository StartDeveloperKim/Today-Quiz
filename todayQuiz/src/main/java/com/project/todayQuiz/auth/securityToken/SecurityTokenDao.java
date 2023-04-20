package com.project.todayQuiz.auth.securityToken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SecurityTokenDao {

    private final RedisTemplate<String, AuthInfo> redisTemplate;

    public void saveTokenInfo(String securityToken, AuthInfo token, Duration duration) {
        ValueOperations<String, AuthInfo> values = redisTemplate.opsForValue();
        values.set(securityToken, token, duration);
    }

    public AuthInfo getTokenInfo(String securityToken) {
        ValueOperations<String, AuthInfo> values = redisTemplate.opsForValue();
        return values.get(securityToken);
    }
}
