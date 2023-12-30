package com.twodollar.tdboard.config;

//import com.twodollar.tdboard.config.handler.UserAuthFailurHandler;
//import com.twodollar.tdboard.modules.auth.controller.JwtUserAuthenticationFilter;
//import com.twodollar.tdboard.modules.auth.service.UserJwtTokenProvider;
//import com.twodollar.tdboard.modules.auth.service.UserPrincipalDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
@Deprecated
public class SecurityCommonConfig {
//
//    @RequiredArgsConstructor
//    @Configuration
//    @Order(1)
//    public class ApiSecurityConfig extends WebSecurityConfigurerAdapter{
//        private final UserJwtTokenProvider userJwtTokenProvider;
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
//            return authenticationConfiguration.getAuthenticationManager();
//        }
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//               .csrf()
//               .disable()
//               .requestMatchers(machers -> {
//                   machers.antMatchers("/api/**");
//               })
//               .authorizeRequests(auth -> auth
//                       .antMatchers("/api/v1/**").hasAnyAuthority("ROLE_USER", "ROLE_ORG", "ROLE_ADMIN")
//                       .anyRequest().authenticated()
//               )
//               .addFilterBefore(new JwtUserAuthenticationFilter(userJwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        }
//
//    }
//
//    @RequiredArgsConstructor
//    @Configuration
//    @Order(2)
//    public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
//        private final UserAuthFailurHandler userAuthFailurHandler;
//        private final UserPrincipalDetailsService userPrincipalDetailsService;
//        @Bean
//        public UserDetailsService UserDetailService(){
//            return new UserPrincipalDetailsService();
//        }
//
//        // 암호화 방식 빈(Bean) 생성
//        @Bean
//        @Primary
//        public BCryptPasswordEncoder UserPasswordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
//        @Bean
//        public DaoAuthenticationProvider homepageAuthenticationProvider(){
//            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//            provider.setUserDetailsService(UserDetailService());
//            provider.setPasswordEncoder(UserPasswordEncoder());
//            return provider;
//        }
//        private final String[] PERMIT_URL_ARRAY = {
//                /* swagger v2 */
//                "/v2/api-docs",
//                "/swagger-resources",
//                "/swagger-resources/**",
//                "/configuration/ui",
//                "/configuration/security",
//                "/swagger-ui.html",
//                "/webjars/**",
//                /* swagger v3 */
//                "/v3/api-docs/**",
//                "/swagger-ui/**"
//        };
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
//                    .and()
//                    .authenticationProvider(homepageAuthenticationProvider())
//                    .authorizeRequests().antMatchers(PERMIT_URL_ARRAY).permitAll()
//                    .and()
//                    .authorizeRequests(
//                            authorize ->
//                                    //authorize.antMatchers("/user/login", "/user/join", "/user/join_proc", "/user/login_proc"
//                                    //                    , "/user/join2", "/user/join2_proc"
//                                    //).authenticated()
//                                    authorize.antMatchers( "/user/login", "/user/join", "/user/join_proc", "/user/login_proc"
//                                                    , "/user/join2", "/user/join2_proc"
//                                            ).permitAll()
//                                            .antMatchers("/org/**").hasAnyAuthority("ROLE_ORG")
//                                            .antMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ORG")
//
//                    )
//                    .formLogin(form -> form
//                            .loginPage("/user/login") // 로그인 페이지 경로 설정(백엔드, 뷰리졸버)
//                            .loginProcessingUrl("/user/login_proc") // 로그인이 실제 이루어지는 곳(백엔드??)
//                            .defaultSuccessUrl("/") // 로그인 성공 후 기본적으로 리다이렉트되는 경로
//                            .failureHandler(userAuthFailurHandler) //실패처리
//                    )
//                    .logout(logout -> logout
//                            .logoutUrl("/user/logout_proc")
//                            .logoutSuccessUrl("/"));
//        }
//    }
}
