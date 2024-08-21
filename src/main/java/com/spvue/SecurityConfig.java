package com.spvue;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable());
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()  // 인증없이 허용할 URL
                        .anyRequest().authenticated()  // 나머지 요청은 인증이 필요
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login") // 로그인 요청을 처리할 URL
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 처리
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("로그인 성공");
                        })
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 처리
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("로그인 실패: " + exception.getMessage());
                        })
                        .permitAll() // 로그인 페이지는 인증 없이 접근 가능
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청을 처리할 URL
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // POST 요청을 사용
                        .logoutSuccessUrl("/")  // 로그아웃 후 리다이렉트할 URL
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}