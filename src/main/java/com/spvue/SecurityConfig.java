package com.spvue;

//import com.spvue.Auth.JWT.JWTFilter;
import com.spvue.Auth.JWT.JWTFilter;
import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberRepository memberRepository;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        );
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/news/post").authenticated() // 인증 필요
                .anyRequest().permitAll() // 그 외 요청 허용
        );
        http.addFilterBefore(new JWTFilter(customUserDetailsService), UsernamePasswordAuthenticationFilter.class); // ✅ 필터 추가

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new DefaultOAuth2UserService() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                try {
                    OAuth2User oAuth2User = super.loadUser(userRequest);

                    // Google OAuth에서 제공하는 기본 정보에서 필요한 값 추출
                    String email = oAuth2User.getAttribute("email");
                    String name = oAuth2User.getAttribute("name");

                    // 사용자 정보를 Member 엔터티로 변환
                    Member member = memberRepository.findByEmail(email).orElseGet(() -> {
                        Member newMember = new Member();
                        newMember.setEmail(email);
                        newMember.setPassword("OAuth");
                        newMember.setUsername(name);
                        newMember.setDisplayName(name);
                        newMember.setPassword(passwordEncoder().encode("1234"));
                        //OAuth유저의 권한
                        newMember.setRole("OAuth");
                        return memberRepository.save(newMember);
                    });
                    // 권한 설정
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));
                    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
                    attributes.put("displayName", member.getDisplayName()); // 추가 속성 설정
                    // CustomUserDetails 인스턴스를 반환
                    return new CustomUserDetails(member, attributes);
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력
                    e.printStackTrace();
                    throw e; // 예외를 다시 던져서 스프링 시큐리티가 처리할 수 있도록 함
                }
            }


        };
    }

}