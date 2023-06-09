package com.project.todayQuiz.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum Role {
    GUEST("ROLE_GUEST", "손님"), ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

    public static Role getRole(String key) {
        if (key.equals("ROLE_GUEST")) {
            return GUEST;
        } else {
            return ADMIN;
        }
    }
}
