package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.service.UserJwtTokenProvider;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

//TODO AuthAdminApiController도 만들어야함
//TODO AuthAdminApiController도 만들어야함
//TODO AuthAdminApiController도 만들어야함
//TODO AuthAdminApiController도 만들어야함
//TODO AuthAdminApiController도 만들어야함
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthUserApiController {
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserRepository userRepository;

    final String BIRTH = "001200";
    final String EMAIL = "aabbcc@gmail.com";
    final String USERNAME = "hinnx27";
    final Long SEQUENCEID = Long.valueOf(1);
    User user = User.builder()
            .username(USERNAME)
            .id(SEQUENCEID)
            .role("ROLE_USER") // 최초 가입시 USER 로 설정
            .build();


    @PostMapping("/api/v1/join")
    public String join(){
        log.info("로그인 시도됨");
        userRepository.save(user);
        return user.toString();

    }

    // 로그인
    @PostMapping("/api/v1/login")
    public String login(@RequestBody Map<String, String> userMap) {
        log.info("user email = {}", userMap.get("username"));
        User user = userRepository.findByUsername(userMap.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));

        return userJwtTokenProvider.createToken(user.getUsername(), Collections.singletonList(user.getRole()));
    }
}
