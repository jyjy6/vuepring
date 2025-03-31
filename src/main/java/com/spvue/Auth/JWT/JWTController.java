package com.spvue.Auth.JWT;

import com.spvue.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입
public class JWTController {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/login/jwt")
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

    @GetMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue("refreshToken") String refreshToken) {
        if (JwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "리프레시 토큰이 만료되었습니다."));
        }
        System.out.println("새액세스토큰 요청됨");
        // 리프레시 토큰에서 사용자 정보(username 또는 userId) 추출
        String username = JwtUtil.extractUsername(refreshToken);
        // 추출한 사용자 정보로 새 액세스 토큰 생성
        String newAccessToken = JwtUtil.createAccessToken(username, customUserDetailsService);
        System.out.println("새액세스토큰 발급됨"+newAccessToken);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // HTTP Only 쿠키 삭제
        Cookie accessCookie = new Cookie("accessToken", null);
        accessCookie.setMaxAge(0);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
