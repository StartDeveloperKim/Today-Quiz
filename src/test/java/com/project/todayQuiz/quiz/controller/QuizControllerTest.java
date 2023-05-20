package com.project.todayQuiz.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.quiz.dto.request.QuizRequest;
import com.project.todayQuiz.quiz.service.QuizService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuizControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    private MockMvc mvc;

    private final String BASE_URL = "http://localhost:";

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizRepository quizRepository;

    private final String QUESTION = "question";
    private final String ANSWER = "answer";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void clearDB() {
        quizRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ADMIN은_퀴즈를_등록할_수_있다() throws Exception {
        //given
        QuizRequest quizRequest = QuizRequest.builder()
                .question(QUESTION)
                .answer(ANSWER)
                .year(2023)
                .month(4)
                .day(30)
                .hour(17)
                .minute(0).build();

        String URL = BASE_URL + port + "/quiz";
        //when
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(quizRequest)))
                .andExpect(status().isOk()).andReturn();
        //then

        List<Quiz> all = quizRepository.findAll();
        Quiz quiz = all.get(0);
        assertThat(quiz.getQuestion()).isEqualTo(QUESTION);
        assertThat(quiz.getAnswer()).isEqualTo(ANSWER);
    }



}