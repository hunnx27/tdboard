package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.auth.service.AuthService;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthJwtTokenProvider authJwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthService authService;
    // 로그인
    @PostMapping("/api/v1/login")
    public String login(@RequestBody Map<String, String> userMap) {
        log.info("user email = {}", userMap.get("username"));

        User user = userRepository.findByUsername(userMap.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));

        return authJwtTokenProvider.createToken(user.getUsername(), Collections.singletonList(user.getRole()));
    }

    @GetMapping("/api/v1/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        // 헤더에서 JWT 를 받아옵니다.
        String authorizationHeader = authJwtTokenProvider.resolveToken((HttpServletRequest) request);
        String[] tokens = authorizationHeader!=null? authorizationHeader.split(" ") : null;
        String refreshToken = tokens!=null ? tokens[tokens.length-1] : null;

        HttpSession session = request.getSession(true);

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 Refresh Token 입니다."));
        Map<String, String> newTokens = authService.refresh(refreshToken);
        String newToken = newTokens.get("accessToken");
        session.setAttribute("accessToken", newToken);
        String newRefreshToken = newTokens.get("refreshToken");
        if (newRefreshToken != null) {
            session.setAttribute("refreshToken", refreshToken);
        }
        return ResponseEntity.ok(newTokens);
    }
}
