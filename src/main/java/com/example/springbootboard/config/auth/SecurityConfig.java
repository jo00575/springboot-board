package com.example.springbootboard.config.auth;

import com.example.springbootboard.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()     // h2-console 화면 사용 위해 해당 옵션 disable
                .and()
                    .authorizeRequests()            //  URL 별 권한 관리 설정 옵션의 시작점
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()   // 설정된 URL 이외 나머지 URL : 인증된 사용자에게 모두 허용
                .and()
                    .logout()
                        .logoutSuccessUrl("/")      // 로그아웃 성공 시 이동할 주소
                .and()
                    .oauth2Login()                  // OAuth2 로그인 기능 설정의 시작점
                        .userInfoEndpoint()         // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                            .userService(customOAuth2UserService);
    }
}
