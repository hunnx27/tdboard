package com.twodollar.tdboard.config;

import com.twodollar.tdboard.config.handler.UserAuthFailurHandler;
import com.twodollar.tdboard.modules.auth.service.UserPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@Order(1)
public class SecurityUserConfig {

    private final UserAuthFailurHandler userAuthFailurHandler;
    private final UserPrincipalDetailsService userPrincipalDetailsService;
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
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authenticationProvider(homepageAuthenticationProvider())
                .antMatcher("/**").authorizeRequests(authorize -> authorize
                    .antMatchers("/user/login", "/user/join", "/user/join_proc", "/user/login_proc"
                            , "/user/join2", "/user/join2_proc"
                    ).permitAll()
                    .antMatchers("/org/**").hasAnyAuthority("ROLE_ORG")
                    .antMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ORG")
                    .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/user/login") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
                        .loginProcessingUrl("/user/login_proc") // 로그인이 실제 이루어지는 곳(백엔드??)
                        .defaultSuccessUrl("/") // 로그인 성공 후 기본적으로 리다이렉트되는 경로
                        .failureHandler(userAuthFailurHandler) //실패처리
                ) 
                
                .logout(logout -> logout
                        .logoutUrl("/user/logout_proc")
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}
