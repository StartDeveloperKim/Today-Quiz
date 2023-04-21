package com.project.todayQuiz.auth.jwt;

import com.project.todayQuiz.auth.jwt.dto.TokenResponse;
import com.project.todayQuiz.auth.jwt.dto.UserInfo;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:application-jwt.properties")
public class TokenProvider {

    private final Key accessTokenSecretKey;
    private final Key refreshTokenSecretKey;
    private final RefreshTokenDao refreshTokenDao;

    private static final String NICKNAME = "nickname";
    private static final String ISSUER = "Today-Quiz";

    @Autowired
    public TokenProvider(@Value("${jwt.access.secret}") String accessKey,
                         @Value("${jwt.refresh.secret}") String refreshKey,
                         RefreshTokenDao refreshTokenDao) {
        byte[] accessKeyBytes = accessKey.getBytes();
        byte[] refreshKeyBytes = refreshKey.getBytes();
        this.accessTokenSecretKey = new SecretKeySpec(accessKeyBytes, "HmacSHA256");
        this.refreshTokenSecretKey = new SecretKeySpec(refreshKeyBytes, "HmacSHA256");

        this.refreshTokenDao = refreshTokenDao;
    }

    public String createAccessToken(String email, String nickname) {
        long accessTokenExpireTime = 1500;
        Date expiryDate = Date.from(Instant.now().plusSeconds(accessTokenExpireTime));
        return createToken(email, nickname, expiryDate, accessTokenSecretKey);
    }

    public String createRefreshToken(String email, String nickname) {
        // 현재는 하루지만 나중에 한달로 고치자
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return createToken(email, nickname, expiryDate, refreshTokenSecretKey);
    }

    public TokenResponse reissueAccessToken(String refreshToken) {
        String email = refreshTokenDao.getEmail(refreshToken);
        UserInfo userInfo = getTokenInfo(refreshToken, TokenType.REFRESH);
        if (email != null && email.equals(userInfo.getEmail())) {
            String newRefreshToken = createRefreshToken(userInfo.getEmail(), userInfo.getNickname());
            refreshTokenDao.saveRefreshToken(newRefreshToken, email, Duration.ofDays(1));
            return new TokenResponse(createAccessToken(userInfo.getEmail(), userInfo.getNickname()), newRefreshToken);
        }
        return null;
    }

    private String createToken(String email, String nickname, Date expiryDate, Key secretKey) {
        return Jwts.builder()
                .signWith(secretKey)
                .setSubject(email)
                .claim(NICKNAME, nickname)
                .setIssuer(ISSUER) // 토큰을 구별하도록 임의로 이름을 지음
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validateToken(String token, TokenType tokenType) {
        try {
            getClaims(token, tokenType);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public UserInfo getTokenInfo(String token, TokenType tokenType) {
        Claims claims = getClaims(token, tokenType);
        String email = claims.getSubject();
        String nickname = claims.get(NICKNAME, String.class);

        return new UserInfo(email, nickname);
    }

    private Claims getClaims(String token, TokenType tokenType) {
        Key secretKey = tokenType.equals(TokenType.ACCESS) ? this.accessTokenSecretKey : this.refreshTokenSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
    }
}
