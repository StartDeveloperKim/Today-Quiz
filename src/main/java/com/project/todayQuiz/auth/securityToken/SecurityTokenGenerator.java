package com.project.todayQuiz.auth.securityToken;

import java.util.UUID;

public class SecurityTokenGenerator {

    public static String generateSecurityToken() {
        return UUID.randomUUID().toString();
    }
}
