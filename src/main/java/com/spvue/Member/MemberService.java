package com.spvue.Member;


import com.spvue.Auth.OAuth.CustomUserDetails;
import com.spvue.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


    public Member registerUser(Member member) {
        // 사용자 이름, 닉네임이 이미 존재하는지 확인
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new RuntimeException("Username already exists");
        } else if (memberRepository.existsByDisplayName(member.getDisplayName())) {
            throw new RuntimeException("Display name already exists");
        }

        // 비밀번호가 비어 있지 않으면 암호화
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty"); // 비밀번호가 없으면 예외 처리
        }
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        member.setRole("USER");


        // 사용자 저장
        return memberRepository.save(member);
    }


    public MemberDto editUser(Member member, Authentication auth) {
        String username = ((CustomUserDetails)auth.getPrincipal()).getUsername();
        if(!username.equals(member.getUsername())){
            throw new IllegalArgumentException("아이디는 수정할 수 없습니다.");
        }

        Member editTargetMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setId(editTargetMember.getId());
        member.setRole(editTargetMember.getRole());

        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            member.setPassword(editTargetMember.getPassword());
        }

        // 사용자 저장
        memberRepository.save(member);
        Optional.ofNullable(member.getProfileImage())
                .ifPresent(img -> imageService.imageFinalSave(new String[] { img }));
        MemberDto userInfo = MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .displayName(member.getDisplayName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .profileImage(member.getProfileImage())
                .country(member.getCountry())
                .mainAddress(member.getMainAddress())
                .subAddress(member.getSubAddress())
                .role(member.getRole())
                .build();
        return userInfo;
    }

    public ResponseEntity<String> validatePw(@RequestBody Map<String, String> requestBody){
        String id = requestBody.get("id"); // id 추출
        String pw = requestBody.get("pw"); // pw 추출

        var targetMember = memberRepository.findByUsername(id)
                .orElseThrow(() -> new RuntimeException("Value not found!"));
        var correctPassword = targetMember.getPassword();
        // 비밀번호 검증
        if (passwordEncoder.matches(pw, correctPassword)) {
            return ResponseEntity.ok().body("Valid");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

    }




    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getUserById(Long id) {
        return memberRepository.findById(id);
    }

    public Member createUser(Member member) {
        return memberRepository.save(member);
    }

    public void deleteUser(Long id) {
        memberRepository.deleteById(id);
    }


}
