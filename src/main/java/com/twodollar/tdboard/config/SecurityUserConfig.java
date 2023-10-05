package com.twodollar.tdboard.config;

import com.twodollar.tdboard.config.auth.UserPrincipalDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(0)
public class SecurityUserConfig {

    @Bean
    public UserDetailsService UserDetailService(){
        return new UserPrincipalDetailsService();
    }

    // 암호화 방식 빈(Bean) 생성
    @Bean
    @Primary
    public BCryptPasswordEncoder UserPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider homepageAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(UserDetailService());
        provider.setPasswordEncoder(UserPasswordEncoder());
        return provider;
    }

    @Bean
    protected SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .antMatcher("/**").authorizeRequests(authorize -> authorize
                    // TODO 시큐리티가 왜 걸리지 않는지 확인해야함
                    // TODO https://github.com/dedel009/securityLogin/blob/master/src/main/java/com/exam/configure/UserSecurityConfiguration.java
                    .antMatchers("/user/loginForm", "/user/joinForm", "/user/join", "/user/login").permitAll()
                    .antMatchers("/user/**").hasAnyAuthority("ROLE_USER")
                    .anyRequest().permitAll())

                .formLogin(form -> form
                        .loginPage("/user/loginForm") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
                        .loginProcessingUrl("/user/login") // 로그인이 실제 이루어지는 곳(백엔드??)
                        .defaultSuccessUrl("/")) // 로그인 성공 후 기본적으로 리다이렉트되는 경로
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}
