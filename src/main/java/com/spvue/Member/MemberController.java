package com.spvue.Member;

import com.spvue.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
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
    private final MemberRepository memberRepository;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Member member) {
        try {
            memberService.registerUser(member);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestBody Member member, Authentication auth) {
        try {
            memberService.editUser(member, auth);
            return new ResponseEntity<>("User edit successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        if (auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        var result = memberRepository.findByUsername(auth.getName());
        Member data = result.get();

        MemberDto userInfo = MemberDto.builder()
                .id(data.getId())
                .username(data.getUsername())
                .displayName(data.getDisplayName())
                .email(data.getEmail())
                .phone(data.getPhone())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .role(data.getRole())
                .build();

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getSessionStatus(HttpSession session, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("세션 유지 중 (인증됨)");
        } else if (session != null && session.getAttribute("user") != null) {
            return ResponseEntity.ok("세션 유지 중 (세션에 사용자 정보 있음)");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
    }


}



