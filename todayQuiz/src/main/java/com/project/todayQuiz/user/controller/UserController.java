package com.project.todayQuiz.user.controller;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.user.dto.NicknameCheckRequest;
import com.project.todayQuiz.user.dto.NicknameCheckResponse;
import com.project.todayQuiz.user.service.UserService;
import com.project.todayQuiz.user.util.CookieType;
import com.project.todayQuiz.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {

    private final TokenProvider tokenProvider;
    private final SecurityTokenDao securityTokenDao;
    private final RefreshTokenDao refreshTokenDao;

    private final UserService userService;

    @GetMapping("/user")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/api/nickname")
    @ResponseBody
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestBody NicknameCheckRequest checkRequest) {
        NicknameCheckResponse nicknameCheckResponse = new NicknameCheckResponse(userService.checkDuplicatedNickname(checkRequest.getNickname()));
        return ResponseEntity.ok(nicknameCheckResponse);
    }

    @PostMapping("/api/logout")
//    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("/logout");
//        TokenResponse tokenResponse = CookieUtil.parseToken(request);
//        Cookie accessTokenCookie = CookieUtil.getAccessTokenCookie(tokenResponse.getAccessToken(), CookieType.EXPIRE);
//        Cookie refreshTokenCookie = CookieUtil.getRefreshTokenCookie(tokenResponse.getRefreshToken(), CookieType.EXPIRE);
//
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
//        return ResponseEntity.ok(Boolean.TRUE);
        return "redirect:/";
    }
//
//    @GetMapping("/api/reissue")
//    public String reissueAccessToken(@CookieValue(value = "refreshToken") String refreshToken,
//                                                            HttpServletResponse response) {
//        log.info("/api/reissue");
//        if (refreshToken != null) {
//            log.info("refreshToken : {}", refreshToken);
//            TokenResponse tokenResponse = tokenProvider.reissueAccessToken(refreshToken);
//            CookieUtil.addTokenCookie(response, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
//        }
//        return "redirect:/";
//    }

    @GetMapping("/auth")
    public String authRedirect(@RequestParam(name = "token") String securityToken,
                               RedirectAttributes attributes) {
        AuthInfo authInfo = securityTokenDao.getTokenInfo(securityToken);
        if (authInfo ==null) {
            return "redirect:/error";
        }
        log.info("/auth, securityToken: {}", securityToken);
        log.info("/auth, accessToken: {}", authInfo.getAccessToken());
        log.info("/auth, refreshToken: {}", authInfo.getRefreshToken());

        attributes.addFlashAttribute("authInfo", authInfo);

        return "redirect:/";
    }
}
