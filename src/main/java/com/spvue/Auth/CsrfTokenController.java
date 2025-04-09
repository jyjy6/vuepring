package com.spvue.Auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class CsrfTokenController {

    @PostMapping("/csrf") // 👈 POST 방식으로 변경
    public ResponseEntity<Void> getCsrfToken() {
        System.out.println("csrf토큰 발행");
        return ResponseEntity.ok().build();
    }
}
