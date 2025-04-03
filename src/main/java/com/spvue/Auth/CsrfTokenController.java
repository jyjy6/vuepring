package com.spvue.Auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class CsrfTokenController {
    @GetMapping("/csrf")
    public ResponseEntity<Void> getCsrfToken() {
        // 이 메서드는 실제로 아무것도 하지 않아도 됩니다.
        // Spring Security가 이미 토큰을 생성하고 쿠키에 설정했기 때문입니다.
        System.out.println("csrf토큰 발행");
        return ResponseEntity.ok().build();
    }
}