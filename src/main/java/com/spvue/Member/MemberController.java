package com.spvue.Member;

import com.spvue.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Member member) {
        try {
            memberService.registerUser(member);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
        // 사용자 정보를 가져오는 로직
        // 예: return ResponseEntity.ok(userService.getUserInfo(principal.getName()));
        return ResponseEntity.ok(Collections.singletonMap("username", principal.getName()));
    }





}
