package com.project.todayQuiz.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@EnableScheduling
@Component
public class RankComponent {

    private final RankRedisDao rankRedisDao;

    @Scheduled(cron = "0 0 0 * * *")
    public void setCount() {
        rankRedisDao.saveCount(0, Duration.ofDays(1));
    }
}
