package com.project.todayQuiz.user.service;

import com.project.todayQuiz.user.dto.LoginResponse;

public interface UserService {

    Boolean checkDuplicatedNickname(String nickname);

    Boolean checkDuplicateEmail(String email);

    Boolean signUp(String email, String password, String nickname);

    LoginResponse login(String email, String password);
}
