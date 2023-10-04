package com.twodollar.tdboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 암호화 방식 빈(Bean) 생성
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                // user 페이지 설정
                .antMatchers("/user/**")
                .authenticated() // 로그인 필요
                // 기타 url은 모두 허용
                .anyRequest()
                .permitAll()
                .and()
                // 로그인 페이지 사용
                .formLogin()
                .loginPage("/loginForm") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
                .loginProcessingUrl("/login") // 로그인이 실제 이루어지는 곳(백엔드??)
                .defaultSuccessUrl("/"); // 로그인 성공 후 기본적으로 리다이렉트되는 경로
    }
}
