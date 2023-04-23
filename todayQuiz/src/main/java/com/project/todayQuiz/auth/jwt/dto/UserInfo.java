package com.project.todayQuiz.auth.jwt.dto;

import com.project.todayQuiz.user.domain.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@ToString
public class UserInfo implements UserDetails {
    private final String email;
    private final String nickname;
    private final Role role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserInfo(String email, String nickname, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.authorities = AuthorityUtils.createAuthorityList(role.getKey());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
