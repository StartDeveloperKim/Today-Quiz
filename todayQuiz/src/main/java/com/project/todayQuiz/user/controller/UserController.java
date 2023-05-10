package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.user.dto.*;
import com.project.todayQuiz.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.RolesAllowed;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {

    private final SecurityTokenDao securityTokenDao;
    private final UserService userService;

    private final String GUEST = "ROLE_GUEST";
    private final String ADMIN = "ROLE_ADMIN";

    @RolesAllowed({GUEST, ADMIN})
    @GetMapping("/user")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/api/nickname")
    @ResponseBody
    public ResponseEntity<DuplicateResponse> checkNickname(@RequestParam(value = "nickname", required = true) String nickname) {
        log.info("닉네임 체크 {}", nickname);
        DuplicateResponse duplicateResponse = new DuplicateResponse(userService.checkDuplicatedNickname(nickname));
        return ResponseEntity.ok(duplicateResponse);
    }

    @GetMapping("/api/email")
    @ResponseBody
    public ResponseEntity<DuplicateResponse> checkEmail(@RequestParam(value = "email", required = true) String email) {
        log.info("이메일 체크 {}", email);
        DuplicateResponse duplicateResponse = new DuplicateResponse(userService.checkDuplicateEmail(email));
        return ResponseEntity.ok(duplicateResponse);
    }

    @GetMapping("/login")
    public String loginForm() {
        log.info("로그인 GET");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest) {
        log.info("로그인 POST");
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signUpForm() {
        return "signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<SignUpResponse> signUpUser(@RequestBody SignUpUserRequest signUpUserRequest) {
        log.info("회원가입 : {}", signUpUserRequest.toString());
        Boolean isSignUp = userService.signUp(signUpUserRequest.getEmail(), signUpUserRequest.getPassword(), signUpUserRequest.getNickname());

        return ResponseEntity.ok(new SignUpResponse(isSignUp));
    }

    @GetMapping("/auth")
    public String authRedirect(@RequestParam(name = "token") String securityToken,
                               RedirectAttributes attributes) {
        AuthInfo authInfo = securityTokenDao.getTokenInfo(securityToken);
        if (authInfo == null) {
            return "redirect:/error";
        }
        attributes.addFlashAttribute("authInfo", authInfo);

        return "redirect:/";
    }
}
