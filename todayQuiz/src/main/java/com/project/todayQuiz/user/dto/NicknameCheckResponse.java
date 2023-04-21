package com.project.todayQuiz.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NicknameCheckResponse {

    private final Boolean isDuplicated;

}
