package com.spvue.Auth.JWT;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spvue.CustomUserDetails;
import com.spvue.CustomUserDetailsService;
import com.spvue.Member.MemberDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String REFRESH_TOKEN_ENDPOINT = "/refresh-token";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("JWT 필터 시작 - 요청 URI: " + request.getRequestURI());
        // refresh-token 요청인 경우 필터 건너뛰기
        if (pathMatcher.match(REFRESH_TOKEN_ENDPOINT, request.getRequestURI())) {
            System.out.println("refresh-token 요청이므로 JWT 필터를 건너뜁니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header: " + authHeader);

        // 요청에서 JWT 추출
        String jwt = getJwtFromRequest(request);

        if (jwt != null) {
            try {
                // JWT에서 Claims 추출
                Claims claims = JwtUtil.extractToken(jwt);
                System.out.println("✅ JWT Claims: " + claims);
                String userInfoJson = claims.get("userInfo", String.class);
                System.out.println("userInfoJson: " + userInfoJson);

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    MemberDto memberDto = objectMapper.readValue(userInfoJson, MemberDto.class);
                    System.out.println("멤버디티오: " + memberDto);

                    String username = memberDto.getUsername();
                    System.out.println("유저네임: " + username);

                    boolean isExpired = JwtUtil.isTokenExpired(jwt);
                    System.out.println("토큰 만료 여부: " + isExpired);

                    // SecurityContext에 인증 정보가 없을 때만 처리
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // UserDetailsService를 사용해 사용자 정보 로드
                        try {
                            System.out.println("사용자 정보 로드 시도: " + username);
                            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
                            System.out.println("사용자 정보 로드 성공: " + customUserDetails);

                            // JWT 유효성 검사
                            if (!JwtUtil.isTokenExpired(jwt)) {
                                // JWT가 유효하면 Authentication 객체 생성
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        customUserDetails, null, customUserDetails.getAuthorities());
                                // 인증 정보를 SecurityContext에 설정
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                System.out.println("인증 객체 설정됨: " + authentication);
                            } else {
                                System.out.println("토큰이 만료되었습니다.");
                            }
                        } catch (Exception e) {
                            System.out.println("사용자 정보 로드 실패: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    System.out.println("인증 객체 설정 확인: " + SecurityContextHolder.getContext().getAuthentication());
                } catch (Exception e) {
                    System.out.println("JSON 파싱 오류: " + e.getMessage());
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user info in JWT");
                    return;
                }
            } catch (Exception e) {
                // JWT 검증 실패 시 예외 처리
                System.out.println("JWT 검증 실패: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return; // 필터 체인 종료
            }
        } else {
            System.out.println("JWT 토큰이 없습니다.");
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