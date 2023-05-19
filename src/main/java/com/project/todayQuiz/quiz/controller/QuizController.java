package com.project.todayQuiz.quiz.controller;

import com.project.todayQuiz.quiz.dto.request.QuizRequest;
import com.project.todayQuiz.quiz.dto.request.QuizUpdateRequest;
import com.project.todayQuiz.quiz.dto.response.ResponseData;
import com.project.todayQuiz.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<ResponseData<Boolean>> add(@RequestBody QuizRequest quizRequest) {
        Long quizId = quizService.postQuiz(quizRequest);
        if (quizId == -1) {
            return ResponseEntity.badRequest().body(new ResponseData<>(Boolean.FALSE, "동일한 날짜에 퀴즈가 이미 존재합니다."));
        }
        return ResponseEntity.ok().body(new ResponseData<>(Boolean.TRUE, null));
    }

    @PatchMapping
    public ResponseEntity<ResponseData<Boolean>> update(@RequestBody QuizUpdateRequest quizRequest) {
        log.info("QuizRequest : {}", quizRequest.toString());
        quizService.updateQuiz(quizRequest);
        return ResponseEntity.ok().body(new ResponseData<>(Boolean.TRUE, "수정이 완료되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        quizService.removeQuiz(id);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
