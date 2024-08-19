package com.spvue.Member;


import lombok.RequiredArgsConstructor;
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
        // 사용자 이름이 이미 존재하는지 확인
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new RuntimeException("Username already exists");
        } else if(memberRepository.existsByDisplayName(member.getDisplayName())){
            throw new RuntimeException("Username already exists");
        }
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
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
