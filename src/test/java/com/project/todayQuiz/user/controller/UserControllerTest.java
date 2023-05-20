package com.project.todayQuiz.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.user.domain.Role;
import com.project.todayQuiz.user.domain.User;
import com.project.todayQuiz.user.domain.UserRepository;
import com.project.todayQuiz.user.dto.NicknameCheckRequest;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final String BASE_URL = "http://localhost:";

    @Autowired
    private SecurityTokenDao securityTokenDao;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        User user = User.builder()
                .picture("picture")
                .email("email123")
                .role(Role.GUEST)
                .authProvider("google")
                .nickname("nickname123").build();
        userRepository.save(user);
    }

    @AfterEach
    void removeDB() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void USER는_닉네임_변경_중복체크를_할_수_있다() throws Exception {
        //given
        String nickname = "nickname";
        String url = BASE_URL + port + "/api/nickname?nickname=" + nickname;
        //when
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(response).isEqualTo("{\"isDuplicated\":false}");
    }


}