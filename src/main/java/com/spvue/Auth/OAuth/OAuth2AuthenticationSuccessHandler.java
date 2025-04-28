package com.spvue.Auth.OAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spvue.Auth.JWT.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        try {


            System.out.println("성공!");

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");

            // JWT 토큰 생성 - 액세스 토큰과 리프레시 토큰 모두 생성
            String accessToken = jwtUtil.createAccessToken(authentication);
            String refreshToken = jwtUtil.createRefreshToken(email);

            // 쿠키 설정 -> 액세스는 필요없음 백엔드엔 유저정보나 뭐 다른거 보내고있고 프론트에서 처리하고있기때문에
//        Cookie accessCookie = new Cookie("accessToken", accessToken);
//        accessCookie.setMaxAge(30 * 1);
//        accessCookie.setHttpOnly(true);
//        accessCookie.setPath("/");
//        accessCookie.setAttribute("SameSite", "None");
//        response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setDomain("ec2-15-164-166-39.ap-northeast-2.compute.amazonaws.com");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setAttribute("SameSite", "None");
            refreshCookie.setSecure(true);  // HTTPS를 사용하는 경우
            response.addCookie(refreshCookie);

            // 방법 1: JSON 응답 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);


            // 방법 2: 프론트엔드로 리다이렉트 (SPA에서 활용)

            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("accessToken", accessToken)
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e){
            System.err.println("인증 성공 핸들러 오류: " + e.getMessage());
            e.printStackTrace();
        }

    }
}