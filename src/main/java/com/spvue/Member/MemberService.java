package com.spvue.Member;


import com.spvue.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


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

        // 사용자 저장
        return memberRepository.save(member);
    }


    public Member editUser(Member member, Authentication auth) {
        String username = auth.getName();
        Member editTargetMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Member not found"));
        member.setId(editTargetMember.getId());
        member.setRole(editTargetMember.getRole());

        // 비밀번호 처리 (이전 설명에서 했던 방법을 적용)
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            member.setPassword(editTargetMember.getPassword());
        }
        // 사용자 저장
        return memberRepository.save(member);
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
