package com.spvue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable());
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()  // 허용할 URL
                        .anyRequest().authenticated()  // 나머지 요청은 인증이 필요
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()  // 로그인 페이지 URL
                )
                .logout(logout -> logout
                        .permitAll()  // 로그아웃 기능
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}