package com.spvue.Auth.OAuth;

import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 구글에서 받아온 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // DB에 있는지 확인, 없으면 새로 생성
        Member member = memberRepository.findByUsername(email)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(email)
                            .username(email)
                            .displayName(name)
                            .password("oauth2user") // 실제로는 사용 안 함
                            .role("USER")
                            .build();
                    return memberRepository.save(newMember);
                });

        // 🔥 핵심! CustomUserDetails로 감싸서 리턴
        return new CustomUserDetails(member, attributes);
    }

}
