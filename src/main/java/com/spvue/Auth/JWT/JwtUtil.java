package com.spvue.Auth.JWT;

import com.spvue.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
            ));

    // JWT 만들어주는 함수
    public static String createAccessToken(Authentication auth) {

        CustomUserDetails member = (CustomUserDetails) auth.getPrincipal();
        String authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(member.getUsername())
                .claim("id", member.getId()) // Long 타입 ID를 클레임으로 추가
                .claim("username", member.getUsername())
                .claim("displayName",member.getDisplayName())
                .claim("role", authorities) //추가함
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //유효기간 1시간
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