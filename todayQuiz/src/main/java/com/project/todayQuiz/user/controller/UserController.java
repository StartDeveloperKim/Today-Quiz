package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.user.dto.NicknameCheckRequest;
import com.project.todayQuiz.user.dto.NicknameCheckResponse;
import com.project.todayQuiz.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RolesAllowed({GUEST, ADMIN})
    @GetMapping("/api/nickname")
    @ResponseBody
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestBody NicknameCheckRequest checkRequest) {

        NicknameCheckResponse nicknameCheckResponse = new NicknameCheckResponse(userService.checkDuplicatedNickname(checkRequest.getNickname()));
        return ResponseEntity.ok(nicknameCheckResponse);
    }

    @GetMapping("/auth")
    public String authRedirect(@RequestParam(name = "token") String securityToken,
                               RedirectAttributes attributes) {
        AuthInfo authInfo = securityTokenDao.getTokenInfo(securityToken);
        if (authInfo ==null) {
            return "redirect:/error";
        }
        attributes.addFlashAttribute("authInfo", authInfo);

        return "redirect:/";
    }
}
