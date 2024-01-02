package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.auth.service.AuthService;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
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
    public ResponseEntity<ApiCmnResponse<?>> login(@RequestBody Map<String, String> userMap) {
        String username = userMap.get("username");
        log.info("user email = {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));
        String accessToken = authJwtTokenProvider.createToken(username, Collections.singletonList(user.getRole()));
        String refreshToken = authJwtTokenProvider.createRefreshToken(username);
        // Refresh Token DB에 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        Map<String, String> newTokens = new HashMap<>();
        newTokens.put("accessToken", accessToken);
        newTokens.put("refreshToken", refreshToken);
        return ResponseEntity.ok(ApiCmnResponse.success(newTokens));
    }

    @GetMapping("/api/v1/refresh")
    public ResponseEntity<ApiCmnResponse<?>> refresh(HttpServletRequest request, HttpServletResponse response) {
        // 헤더에서 JWT 를 받아옵니다.
        String authorizationHeader = authJwtTokenProvider.resolveToken((HttpServletRequest) request);
        String[] tokens = authorizationHeader!=null? authorizationHeader.split(" ") : null;
        String refreshToken = tokens!=null ? tokens[tokens.length-1] : null;

        HttpSession session = request.getSession(true);

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 Refresh Token 입니다."));
        Map<String, String> newTokens = null;
        try {
            newTokens = authService.refresh(refreshToken);
            String newToken = newTokens.get("accessToken");
            session.setAttribute("accessToken", newToken);
            String newRefreshToken = newTokens.get("refreshToken");
            if (newRefreshToken != null) {
                session.setAttribute("refreshToken", refreshToken);
            }
            return ResponseEntity.ok(ApiCmnResponse.success(newTokens));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getMessage()));
        }
    }
}
