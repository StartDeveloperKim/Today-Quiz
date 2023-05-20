package com.project.todayQuiz.quiz.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    Quiz savedQuiz;

    @BeforeEach
    void setUp() {
        Quiz quiz = Quiz.builder()
                .question("question")
                .answer("answer")
                .createDate(LocalDateTime.now())
                .postDate(LocalDateTime.of(2023, 4, 23, 1, 44))
                .build();
        savedQuiz = quizRepository.save(quiz);
    }

    @AfterEach
    void removeDB() {
        quizRepository.deleteAll();
    }

    @Test
    void postDate를_통해_퀴즈를_찾아온다() {
        //when
        LocalDate postDate = LocalDate.of(2023, 4, 23);
        Quiz findQuiz = quizRepository
                .findQuizByPostDate(postDate.getYear(), postDate.getMonthValue(), postDate.getDayOfMonth()).get();
        //then
        assertThat(savedQuiz.getId()).isEqualTo(findQuiz.getId());
    }

    @Test
    void 동일한_PostDate의_퀴즈가있다면_True를_반환한다() {
        //when
        LocalDate postDate = LocalDate.of(2023, 4, 23);
        Boolean result = quizRepository
                .existsQuizByPostDate(postDate.getYear(), postDate.getMonthValue(), postDate.getDayOfMonth());
        //then
        assertThat(result).isTrue();
    }

    @Test
    void 오늘날짜와_정답을_전달하여_정답이_있는지_확인한다() {
        //given
        //when
        Boolean result = quizRepository.checkAnswer(2023, 4, 23, "answer");
        //then
        assertThat(result).isTrue();
    }

}