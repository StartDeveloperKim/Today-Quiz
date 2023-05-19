package com.project.todayQuiz.auth.oauth;

import com.project.todayQuiz.user.domain.Role;
import com.project.todayQuiz.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    private final String picture;
    private final String authProvider;

    @Builder
    private OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, String picture, String authProvider) {
        this.attributes = Optional.ofNullable(attributes).orElse(new HashMap<>());
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.picture = picture;
        this.authProvider = authProvider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .authProvider(registrationId)
                .build();
    }

    public User toEntity(String nickname) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .picture(picture)
                .role(Role.GUEST)
                .authProvider(authProvider)
                .build();
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }
}
