package com.project.todayQuiz.user.service;

public interface UserService {

    Boolean checkDuplicatedNickname(String nickname);

    Boolean checkDuplicateEmail(String email);

    Boolean signUp(String email, String password, String nickname);
}
