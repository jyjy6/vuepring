package com.spvue.Auth;


import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class CsrfTokenController {


    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token; // 자동으로 XSRF-TOKEN 쿠키 내려줌
    }

}
