package com.project.todayQuiz.config;

import com.project.todayQuiz.auth.CustomLogoutSuccessHandler;
import com.project.todayQuiz.auth.jwt.JwtAuthenticationFilter;
import com.project.todayQuiz.auth.jwt.RefreshTokenService;
import com.project.todayQuiz.auth.jwt.TokenProvider;
import com.project.todayQuiz.auth.jwt.refreshToken.RefreshTokenDao;
import com.project.todayQuiz.auth.oauth.OAuthSuccessHandler;
import com.project.todayQuiz.auth.oauth.OAuthUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final OAuthUserServiceImpl oAuthUserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final TokenProvider tokenProvider;
    private final RefreshTokenDao refreshTokenDao;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()

                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/api/logout", "/oauth2/**", "/auth").permitAll()
                .antMatchers("/answer", "/user").hasAnyRole("ROLE_GUEST", "ROLE_ADMIN")
                .antMatchers("/quiz").hasAnyRole("ROLE_ADMIN")
                //, "/login", "/logout", "/oauth2/**", "/auth"
//                .anyRequest().authenticated()

//                .and()
//                .oauth2Login()
//                .redirectionEndpoint()
//                .baseUri("/oauth2/callback/*")

//                .and()
//                .authorizationEndpoint()
//                .baseUri("/auth/authorize")

                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(oAuthUserService)

                .and()
                    .successHandler(oAuthSuccessHandler)

                .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .deleteCookies("accessToken", "refreshToken")
                    .logoutSuccessUrl("/")


                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())

                .and()
                .addFilterAfter(new JwtAuthenticationFilter(tokenProvider, refreshTokenDao), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .regexMatchers("/api/reissue");
    }
}
