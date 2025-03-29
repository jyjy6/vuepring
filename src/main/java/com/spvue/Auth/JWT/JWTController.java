package com.spvue.Auth.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login/jwt")
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입
public class JWTController {

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<Map<String, String>> loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    data.get("username"), data.get("password")
            );

            // AuthenticationManager를 사용하여 인증 수행
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 인증된 사용자 정보 가져오기
            var auth2 = SecurityContextHolder.getContext().getAuthentication();

            // JWT 생성
            String accessToken = JwtUtil.createAccessToken(auth2);
            String refreshToken = JwtUtil.createRefreshToken(auth2.getName());

            // 쿠키 설정
            Cookie accessCookie = new Cookie("accessToken", accessToken);
            accessCookie.setMaxAge(60 * 60); // 1시간
            accessCookie.setHttpOnly(true);
            accessCookie.setPath("/");
            accessCookie.setAttribute("SameSite", "None");
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setMaxAge(60 * 60 * 7); // 7시간
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // 응답 바디 구성
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);
            responseBody.put("refreshToken", refreshToken);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 실패: " + e.getMessage()));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue("refreshToken") String refreshToken, Authentication auth) {
        if (JwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "리프레시 토큰이 만료되었습니다."));
        }

        String newAccessToken = JwtUtil.createAccessToken(auth);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
