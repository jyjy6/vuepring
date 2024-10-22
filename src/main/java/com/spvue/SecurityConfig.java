package com.spvue;

import com.spvue.Member.Member;
import com.spvue.Member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final MemberRepository memberRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable());
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 나머지 요청도 허용
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login") // 로그인 요청을 처리할 URL
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 처리
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("로그인 성공");
//                            System.out.println("Authenticated user: " + authentication.getName());
//                            System.out.println("Authorities: " + authentication.getAuthorities());
                        })
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 처리
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("로그인 실패: " + exception.getMessage());
                            request.getSession(false);
                        })
                        .permitAll() // 로그인 페이지는 인증 없이 접근 가능
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // 커스텀 로그인 페이지
                        .defaultSuccessUrl("http://localhost:8081/?loggedIn=true", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())  // 커스텀 OAuth2UserService 적용
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청을 처리할 URL
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // POST 요청을 사용
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                ).httpBasic(Customizer.withDefaults())  // 기본 인증 활성화
                .userDetailsService(userDetailsService); // CustomUserDetailsService 사용


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