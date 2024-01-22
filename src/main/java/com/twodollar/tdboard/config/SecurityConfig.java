package com.twodollar.tdboard.config;

import com.twodollar.tdboard.config.handler.AuthFailurHandler;
import com.twodollar.tdboard.config.handler.AuthSuccessHandler;
import com.twodollar.tdboard.modules.auth.controller.JwtAuthenticationFilter;
import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.auth.service.AuthPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.Cookie;


@RequiredArgsConstructor
@Configuration
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private final AuthFailurHandler authFailurHandler;
    private final AuthSuccessHandler authSuccessHandler;

    @Bean
    public UserDetailsService UserDetailService(){
        return new AuthPrincipalDetailsService();
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
    @Order(2)
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                //.and()
                .authenticationProvider(homepageAuthenticationProvider())
                .authorizeRequests().antMatchers(PERMIT_URL_ARRAY).permitAll()
                .and()
                .authorizeRequests(
                        authorize ->
                            //authorize.antMatchers("/user/login", "/user/join", "/user/join_proc", "/user/login_proc"
                            //                    , "/user/join2", "/user/join2_proc"
                            //).authenticated()
                            authorize.antMatchers("/auth/login", "/auth/join", "/auth/join_proc", "/auth/login_proc"
                                                , "/auth/join2", "/auth/join2_proc"
                                                , "/auth/join3", "/auth/join3_proc"

                                    // TODO 인증타는지 확인해야함..
                                    //, "/api/v1/auth/join"


                            ).permitAll()
                            .antMatchers("/org/**").hasAnyAuthority("ROLE_ORG", "ROLE_ADMIN")
                            .antMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ORG", "ROLE_ADMIN")
                            .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")

                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
                        .loginProcessingUrl("/auth/login_proc") // 로그인이 실제 이루어지는 곳(백엔드??)
                        .usernameParameter("userId")
                        .defaultSuccessUrl("/") // 로그인 성공 후 기본적으로 리다이렉트되는 경로
                        .successHandler(authSuccessHandler) // 성공 후 처리
                        .failureHandler(authFailurHandler)  // 실패 시 처리
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout_proc")
                        .addLogoutHandler((request, response, auth) -> {
                            for(Cookie cookie : request.getCookies()){
                                String cookieName = cookie.getName();
                                if(cookieName.contains("Token")){
                                    Cookie cookieToDelete = new Cookie(cookieName, null);
                                    cookieToDelete.setMaxAge(0);
                                    cookieToDelete.setPath("/");
                                    response.addCookie(cookieToDelete);
                                }
                            }
                        })
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    private final AuthJwtTokenProvider userJwtTokenProvider;
    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf()
                .disable()
                .requestMatchers(matchers -> matchers.antMatchers("/api/**"))
                .addFilterBefore(new JwtAuthenticationFilter(userJwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/api/v1/refresh",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
