package com.twodollar.tdboard.config;

import com.twodollar.tdboard.modules.auth.controller.JwtAdminAuthenticationFilter;
import com.twodollar.tdboard.modules.auth.service.AdminJwtTokenProvider;
import com.twodollar.tdboard.modules.auth.service.AdminPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@Order(2)
public class SecurityAdminConfig {

    @Bean
    public UserDetailsService adminDetailService(){
        return new AdminPrincipalDetailsService();
    }

    // 암호화 방식 빈(Bean) 생성
    @Bean
    @Qualifier // SecurityUserConfig의 패스워드인코딩 공통으로 사용하는 옵션
    public BCryptPasswordEncoder adminPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminDetailService());
        provider.setPasswordEncoder(adminPasswordEncoder());
        return provider;
    }

    @Bean
    @Order(2)
    protected SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
                .authenticationProvider(adminAuthenticationProvider())
                .antMatcher("/admin/**").authorizeRequests(authorize -> authorize
                    .antMatchers("/admin/login", "/admin/join", "/admin/join_proc", "/admin/login_proc").permitAll()
                    .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                    )

            .formLogin(form -> form
                    .loginPage("/admin/login") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
                    .loginProcessingUrl("/admin/login_proc") // 로그인이 실제 이루어지는 곳(백엔드??)
                    .defaultSuccessUrl("/admin")) // 로그인 성공 후 기본적으로 리다이렉트되는 경로
            .logout(logout -> logout
                    .logoutUrl("/admin/logout_proc")
                    .logoutSuccessUrl("/admin"));
        return http.build();
    }

    private final AdminJwtTokenProvider adminJwtTokenProvider;
    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf()
                .disable()
                .requestMatchers(machers -> {
                    machers.antMatchers("/api/**");
                })
                .authorizeRequests(auth -> auth
                        .antMatchers("/api/v1/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAdminAuthenticationFilter(adminJwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
