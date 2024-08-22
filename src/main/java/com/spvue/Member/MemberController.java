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

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        if (auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        var result = memberRepository.findByUsername(auth.getName());
        var data = result.get();

        MemberDto userInfo = new MemberDto( data.getUsername(), data.getDisplayName(),
                                            data.getEmail(), data.getPhone(),
                                            data.getCreatedAt(), data.getUpdatedAt(), data.getRole());
        return ResponseEntity.ok(userInfo);
    }

}



