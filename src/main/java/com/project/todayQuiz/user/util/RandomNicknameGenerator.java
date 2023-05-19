package com.project.todayQuiz.user.util;

import org.springframework.stereotype.Component;

import java.util.Random;

public class RandomNicknameGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NICKNAME_LENGTH = 10;

    public static String generateNickname() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
