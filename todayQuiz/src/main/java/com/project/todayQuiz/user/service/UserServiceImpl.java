package com.project.todayQuiz.user.service;

import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.securityToken.AuthInfo;
import com.project.todayQuiz.auth.securityToken.SecurityTokenDao;
import com.project.todayQuiz.auth.securityToken.SecurityTokenGenerator;
import com.project.todayQuiz.user.domain.Role;
import com.project.todayQuiz.user.domain.User;
import com.project.todayQuiz.user.domain.UserRepository;
import com.project.todayQuiz.user.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final SecurityTokenDao securityTokenDao;

    @Transactional(readOnly = true)
    @Override
    public Boolean checkDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public Boolean signUp(String email, String password, String nickname) {
        Boolean isDuplicatedNickname = checkDuplicatedNickname(nickname);
        Boolean isDuplicatedEmail = checkDuplicateEmail(email);
        if (!isDuplicatedEmail && !isDuplicatedNickname) {
            User user = User.builder()
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(password))
                    .nickname(nickname)
                    .role(Role.GUEST).build();
            userRepository.save(user);
            return Boolean.TRUE; // 회원가입 성공
        }
        return Boolean.FALSE; // 회원가입 실패
    }

    @Override
    public LoginResponse login(String email, String password) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            User user = findUser.get();
            String originPassword = user.getPassword();
            String userEmail = user.getEmail();
            String userNickname = user.getNickname();
            Role role = user.getRole();

            if (bCryptPasswordEncoder.matches(password, originPassword)) {
                String accessToken = tokenProvider.createAccessToken(userEmail, userNickname, role);
                String refreshToken = tokenProvider.createRefreshToken(userEmail, userNickname, role);

                AuthInfo authInfo = new AuthInfo(accessToken, refreshToken, userEmail, userNickname, role);

                String securityToken = SecurityTokenGenerator.generateSecurityToken();

                securityTokenDao.saveTokenInfo(securityToken, authInfo, Duration.ofSeconds(60));
                return new LoginResponse(Boolean.TRUE, securityToken); // 로그인 성공
            }
        }
        return new LoginResponse(Boolean.FALSE, ""); // 로그인 실패
    }
}
