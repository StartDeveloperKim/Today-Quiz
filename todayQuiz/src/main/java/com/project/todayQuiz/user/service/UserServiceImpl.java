package com.project.todayQuiz.user.service;

import com.project.todayQuiz.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Boolean checkDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
