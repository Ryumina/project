package com.spring.toyproject.springboot.config.auth;

import com.spring.toyproject.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 스프링 시큐리티에서 csrf protection은 default 이다.
        // 보안 수준을 향상시키는 csrf 를 왜 disable 할까?
        // -> non-browser clients 만을 위한 서비스라면 csrf를 disable 하여도 좋다.
        // -> rest api를 사용하는 서버는 session 기반 인증과 달리 서버에 인증 정보를 보관하지 않기 때문에 disable 해도 좋다.
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**")
                    .permitAll().antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
