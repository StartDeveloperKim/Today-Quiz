package com.project.todayQuiz.answer.controller;

import com.project.todayQuiz.answer.dto.AnswerRequest;
import com.project.todayQuiz.answer.dto.AnswerResponse;
import com.project.todayQuiz.answer.dto.AnswerState;
import com.project.todayQuiz.answer.service.AnswerService;
import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;

import static com.project.todayQuiz.answer.dto.AnswerState.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_GUEST')")
    @PostMapping
    public ResponseEntity<AnswerResponse> checkAnswer(@RequestBody AnswerRequest answerRequest,
                                         @AuthenticationPrincipal UserInfo userInfo) {
        AnswerResponse answerResponse = answerService.checkAnswer(answerRequest.getAnswer(), LocalDateTime.now(), userInfo.getEmail());

        return ResponseEntity.ok(answerResponse);
    }
}
