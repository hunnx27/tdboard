package com.twodollar.tdboard.config;

import com.twodollar.tdboard.modules.auth.controller.JwtUserAuthenticationFilter;
import com.twodollar.tdboard.modules.auth.service.UserJwtTokenProvider;
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
@Order(3)
public class SecurityUserJWTConfig {
    private final UserJwtTokenProvider userJwtTokenProvider;

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/v1/**").hasAnyAuthority("ROLE_USER", "ROLE_ORG")
                .and()
                .addFilterBefore(new JwtUserAuthenticationFilter(userJwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
