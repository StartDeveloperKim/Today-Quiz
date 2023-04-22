package com.project.todayQuiz.auth.oauth;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.auth.securityToken.SecurityTokenGenerator;
import com.project.todayQuiz.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final SecurityTokenDao securityTokenDao;

    private static final String ERROR_REDIRECT_URL = "http://localhost:8080/error";
    private static final String REDIRECT_URL = "http://localhost:8080/auth?token=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            log.info("OAuth2 Success Handler running...");

            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = customOAuth2User.getAttributes();

            String email = (String) attributes.get("email");
            String nickname = customOAuth2User.getNickname();

            Optional<Object> roleInfo = Arrays.stream(customOAuth2User.getAuthorities().toArray()).findFirst();
            SimpleGrantedAuthority simpleGrantedAuthority = (SimpleGrantedAuthority) roleInfo.get();
            String role = simpleGrantedAuthority.getAuthority().toString();

            String accessToken = tokenProvider.createAccessToken(email, nickname, Role.getRole(role));
            String refreshToken = tokenProvider.createRefreshToken(email, nickname, Role.getRole(role));

            AuthInfo authInfo = new AuthInfo(accessToken, refreshToken, email, nickname, Role.getRole(role));
            String securityToken = SecurityTokenGenerator.generateSecurityToken();
            securityTokenDao.saveTokenInfo(securityToken, authInfo, Duration.ofSeconds(60));

            log.info("AuthInfo : {}", authInfo.toString());

            String authRedirectURL = REDIRECT_URL + securityToken;
            response.sendRedirect(authRedirectURL);
        }else {
            response.sendRedirect(ERROR_REDIRECT_URL); // 에러
        }
    }
}
