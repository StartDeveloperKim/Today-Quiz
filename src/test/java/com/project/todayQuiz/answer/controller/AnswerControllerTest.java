package com.project.todayQuiz.answer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todayQuiz.answer.dto.AnswerRequest;
import com.project.todayQuiz.answer.dto.AnswerResponse;
import com.project.todayQuiz.answer.dto.AnswerState;
import com.project.todayQuiz.answer.service.AnswerService;
import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.securityTest.WithAuthUser;
import com.project.todayQuiz.user.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static com.project.todayQuiz.answer.dto.AnswerState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {


    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AnswerService answerService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    @WithAuthUser(email = "email", nickname = "nickname", role="ROLE_GUEST")
    void GUEST는_오늘의퀴즈의_정답을_제출할_수_있다() throws Exception {
        //given
        AnswerResponse answerResponse = new AnswerResponse(CORRECT.name(), CORRECT.getStateMessage(), 1);
        doReturn(answerResponse).when(answerService).checkAnswer(anyString(), any(), anyString());

        //when
        ResultActions perform = mvc.perform(post("/answer")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(answerResponse)));
        //then
        perform.andExpect(status().isOk());
    }
}