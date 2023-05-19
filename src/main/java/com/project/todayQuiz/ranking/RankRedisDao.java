package com.project.todayQuiz.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RankRedisDao {

    private final RedisTemplate<String, Integer> redisTemplate;
    private static final String RANKING_KEY = "ranking";

    public void saveCount(Integer count, Duration duration) {
        ValueOperations<String, Integer> values = redisTemplate.opsForValue();
        values.set(RANKING_KEY, count, duration);
    }

    public Integer getCount() {
        ValueOperations<String, Integer> values = redisTemplate.opsForValue();
        return values.get(RANKING_KEY);
    }
}
