package com.project.todayQuiz.config;

import org.springframework.boot.test.context.TestConfiguration;

import javax.annotation.PostConstruct;

@TestConfiguration
public class TestConfig {

    @PostConstruct
    public void init() {
        System.setProperty("spring.profiles.active", "dev");
    }
}
