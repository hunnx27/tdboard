package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthJwtTokenProvider userJwtTokenProvider;
    private final UserRepository userRepository;

    // 로그인
    @PostMapping("/api/v1/login")
    public String login(@RequestBody Map<String, String> userMap) {
        log.info("user email = {}", userMap.get("username"));

        User user = userRepository.findByUsername(userMap.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));

        return userJwtTokenProvider.createToken(user.getUsername(), Collections.singletonList(user.getRole()));
    }
}
