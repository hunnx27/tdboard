package com.twodollar.tdboard.config;

import com.twodollar.tdboard.modules.auth.controller.JwtAdminAuthenticationFilter;
import com.twodollar.tdboard.modules.auth.service.AdminJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@Order(4)
public class SecurityAdminJWTConfig {
    private final AdminJwtTokenProvider adminJwtTokenProvider;

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .antMatcher("/api/**/admin/**").authorizeRequests(
                        authorize -> authorize
                                        //.antMatchers("/").permitAll()
                                        .antMatchers("").hasAnyAuthority("ROLE_ADMIN")
                                        .anyRequest().permitAll())
                .addFilterBefore(new JwtAdminAuthenticationFilter(adminJwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
