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
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:application-jwt.properties")
public class TokenProvider {

    private final Key secretKey;
    private final RefreshTokenDao refreshTokenDao;

    private static final String NICKNAME = "nickname";
    private static final String ISSUER = "Today-Quiz";

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String key, RefreshTokenDao refreshTokenDao) {
        byte[] bytes = key.getBytes();
        this.secretKey = new SecretKeySpec(bytes, "HmacSHA256");
        this.refreshTokenDao = refreshTokenDao;
    }

    public String createAccessToken(String email, String nickname) {
        long accessTokenExpireTime = 1500;
        Date expiryDate = Date.from(Instant.now().plusSeconds(accessTokenExpireTime));
        return createToken(email, nickname, expiryDate);
    }

    public String createRefreshToken(String email, String nickname) {
        // 현재는 하루지만 나중에 한달로 고치자
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return createToken(email, nickname, expiryDate);
    }

    public TokenResponse reissueAccessToken(String refreshToken) {
        String email = refreshTokenDao.getEmail(refreshToken);
        UserInfo userInfo = getTokenInfo(refreshToken);
        if (email != null && email.equals(userInfo.getEmail())) {
            String newRefreshToken = createRefreshToken(userInfo.getEmail(), userInfo.getNickname());
            refreshTokenDao.saveRefreshToken(newRefreshToken, email, Duration.ofDays(1));
            return new TokenResponse(createAccessToken(userInfo.getEmail(), userInfo.getNickname()), newRefreshToken);
        }
        return null;
    }

    private String createToken(String email, String nickname, Date expiryDate) {
        return Jwts.builder()
                .signWith(secretKey)
                .setSubject(email)
                .claim(NICKNAME, nickname)
                .setIssuer(ISSUER) // 토큰을 구별하도록 임의로 이름을 지음
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
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

    public UserInfo getTokenInfo(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        String nickname = claims.get(NICKNAME, String.class);

        return new UserInfo(email, nickname);
    }

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build().parseClaimsJws(token).getBody();
    }
}
