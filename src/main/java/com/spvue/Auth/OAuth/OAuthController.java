package com.spvue.Auth.OAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spvue.Auth.JWT.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {
    @Value("${GOOGLE_CLIENT_ID}")
    String clientId;
    @Value("${GOOGLE_REDIRECT_URI}")
    String redirectUri;
    private final JwtUtil jwtUtil;



    // JWT 토큰 테스트용 엔드포인트
    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            // 쿠키에서 accessToken 가져오기
            Cookie[] cookies = request.getCookies();
            String accessToken = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("액세스 토큰이 없습니다.");
            }

            // 토큰 검증 및 유저 정보 추출
            Claims claims = jwtUtil.extractToken(accessToken);
            String userInfoJson = claims.get("userInfo", String.class);

            // JSON을 그대로 반환하거나 객체로 변환해서 반환
            ObjectMapper objectMapper = new ObjectMapper();
            Object userInfo = objectMapper.readValue(userInfoJson, Object.class);

            return ResponseEntity.ok(userInfo);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }


}
