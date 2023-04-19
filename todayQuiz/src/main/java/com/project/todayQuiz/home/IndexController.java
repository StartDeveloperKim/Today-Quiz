package com.project.todayQuiz.home;

import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public String home(Model model) {
        TokenResponse tokenResponse = (TokenResponse) model.asMap().get("tokenResponse");
        if (tokenResponse != null){
            log.info("/index, {}", tokenResponse.toString());
        }
        return "index";
    }
}
