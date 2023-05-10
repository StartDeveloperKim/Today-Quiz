package com.project.todayQuiz.user.service;

import com.project.todayQuiz.user.domain.Role;
import com.project.todayQuiz.user.domain.User;
import com.project.todayQuiz.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    @Override
    public Boolean checkDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public Boolean signUp(String email, String password, String nickname) {
        Boolean isDuplicatedNickname = checkDuplicatedNickname(nickname);
        Boolean isDuplicatedEmail = checkDuplicateEmail(email);
        if (!isDuplicatedEmail && !isDuplicatedNickname) {
            User user = User.builder()
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(password))
                    .nickname(nickname)
                    .role(Role.GUEST).build();
            userRepository.save(user);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
