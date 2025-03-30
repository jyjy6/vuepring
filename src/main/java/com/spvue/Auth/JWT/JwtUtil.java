package com.spvue.Auth.JWT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spvue.CustomUserDetails;
import com.spvue.Member.Member;
import com.spvue.Member.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.key}")
    private String jwtKey; // application.properties에서 값 주입

    private static SecretKey key; // static 유지, final 제거

    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }

    // JWT 만들어주는 함수
    public static String createAccessToken(Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Member member = userDetails.getMember();
        String authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));
        // 원하는 정보만 포함한 DTO 생성
        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .displayName(member.getDisplayName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .createdAt(member.getCreatedAt())
                .role(authorities)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String memberJson = "";
        try {
            memberJson = objectMapper.writeValueAsString(memberDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting MemberDto to JSON", e);
        }



        return Jwts.builder()
                .setSubject(member.getUsername())
                .claim("userInfo", memberJson) // JSON 형태로 저장
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    public static String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7일 유효
                .signWith(key)
                .compact();
    }

    // JWT 까주는 함수
    public static Claims extractToken (String token){
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }


    //JWT 토큰에서 클레임(Claims)을 추출하는 기능을 수행
    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //이 메서드는 JWT 토큰이 만료되었는지 여부를 확인하는 기능을 수행
    public static boolean isTokenExpired(String token) {
        final Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    //이 메서드는 JWT 토큰에서 사용자 이름을 추출하는 기능을 수행
    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }


}