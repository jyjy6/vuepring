package com.spvue.Sales;


import com.spvue.CustomUserDetails;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService salesService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Sales sales,
                                                Authentication auth) {
        salesService.save(sales, auth);
        return ResponseEntity.ok("Post created");
    }


}
