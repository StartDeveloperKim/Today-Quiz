package com.project.todayQuiz.user.dto;

import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserRequest {

    // TODO :: Bean Validation 적용시켜서 형식에 맞지 않다면 다시 되돌리기

    private String email;
    private String password;
    private String nickname;
}
