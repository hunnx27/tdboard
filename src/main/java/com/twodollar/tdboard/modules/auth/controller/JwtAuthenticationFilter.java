package com.twodollar.tdboard.modules.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

// 요청시 JWT 인증 구간
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthJwtTokenProvider authJwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String authorizationHeader = authJwtTokenProvider.resolveToken((HttpServletRequest) request);
        String[] tokens = authorizationHeader!=null? authorizationHeader.split(" ") : null;
        String token = tokens!=null ? tokens[tokens.length-1] : null;
        // 유효한 토큰인지 확인합니다.
        if (token != null) {
            try{
                authJwtTokenProvider.validateToken(token);
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = authJwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (MalformedJwtException e) {
                log.info("잘못된 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.info("CustomAuthorizationFilter : Access Token이 만료되었습니다.");
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ApiCmnResponse errorResponse =  ApiCmnResponse.error("401","Access Token이 만료되었습니다.(401)");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                log.info("만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.info("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : {}", e.getMessage());
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ApiCmnResponse errorResponse = ApiCmnResponse.error("400", "잘못된 JWT Token 입니다.(400)");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                log.info("지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                log.info("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : {}", e.getMessage());
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ApiCmnResponse errorResponse = ApiCmnResponse.error("400", "잘못된 JWT Token 입니다.(400)");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                log.info("JWT 토큰이 잘못되었습니다.(IllegalArgumentException)");
            } catch (Exception e){
                log.info("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : {}", e.getMessage());
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ApiCmnResponse errorResponse = ApiCmnResponse.error("400", "잘못된 JWT Token 입니다.(400)");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                log.info("JWT 토큰이 잘못되었습니다.(Exception)");
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}