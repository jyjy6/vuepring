package com.spvue.Auth.JWT;

import com.spvue.Auth.OAuth.CustomUserDetailsService;
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

            // 쿠키 설정 액세스토큰은 localstorage에 저장하기때문에 굳이 필요없음.
            // ->csrf 방어를위해 없는게 나음
//            Cookie accessCookie = new Cookie("accessToken", accessToken);
//            accessCookie.setMaxAge(60 * 60);
//            accessCookie.setHttpOnly(true);
//            accessCookie.setPath("/");
//            accessCookie.setAttribute("SameSite", "None");
//            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // 응답 바디 구성
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 실패: " + e.getMessage()));
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        System.out.println("새 액세스 토큰 요청됨");

        try {
            // 리프레시 토큰이 없는 경우
            if (refreshToken == null || refreshToken.isEmpty()) {
                System.out.println("리프레시 토큰이 없음");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "리프레시 토큰이 존재하지 않습니다."));
            }
            // 리프레시 토큰 만료 확인
            if (JwtUtil.isTokenExpired(refreshToken)) {
                System.out.println("토큰 만료됨");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "리프레시 토큰이 만료되었습니다."));
            }

            // 리프레시 토큰에서 사용자 정보(username 또는 userId) 추출
            String username = JwtUtil.extractUsername(refreshToken);
            // 추출한 사용자 정보로 새 액세스 토큰 생성
            String newAccessToken = JwtUtil.createAccessToken(username, customUserDetailsService);
            System.out.println("새 액세스 토큰 발급됨: " + newAccessToken);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (Exception e) {
            System.out.println("토큰 갱신 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류로 인해 액세스 토큰을 갱신할 수 없습니다."));
        }
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
