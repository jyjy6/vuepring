package com.spvue.Auth.JWT;


import com.spvue.CustomUserDetails;
import com.spvue.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 요청에서 JWT 추출
        String jwt = getJwtFromRequest(request);

        if (jwt != null) {
            try {
                // JWT에서 Claims 추출
                Claims claims = JwtUtil.extractToken(jwt);

                // 사용자 이름 추출
                String username = claims.get("username", String.class);

                // SecurityContext에 인증 정보가 없을 때만 처리
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // UserDetailsService를 사용해 사용자 정보 로드
                    CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
                    // JWT 유효성 검사
                    if (!JwtUtil.isTokenExpired(jwt)) {
                        // JWT가 유효하면 Authentication 객체 생성
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                customUserDetails, null, customUserDetails.getAuthorities());
                        // 인증 정보를 SecurityContext에 설정
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // JWT 검증 실패 시 예외 처리
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return; // 필터 체인 종료
            }
        }
        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    // 요청에서 JWT를 추출하는 메서드
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }
}
