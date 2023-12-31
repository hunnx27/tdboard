package com.twodollar.tdboard.modules.auth.service;

import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository; // 글 아래에서 생성할 예정
    private final BCryptPasswordEncoder passwordEncoder; // 시큐리티에서 빈(Bean) 생성할 예정
    private final AuthJwtTokenProvider authJwtTokenProvider;

    public void join(User user){
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void join_orgtest(User user){
        user.setRole("ROLE_ORG");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void join_admintest(User user){
        user.setRole("ROLE_ADMIN");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Map<String, String> refresh(String refreshToken){
        Map<String, String> accessTokenResponseMap = new HashMap<>();
        try {
            authJwtTokenProvider.validateToken(refreshToken);
            String userName = authJwtTokenProvider.getUserPk(refreshToken);
            User user = userRepository.findByUsername(userName).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));
            // === Access Token 재발급 === //
            long now = System.currentTimeMillis();
            if (!user.getRefreshToken().equals(refreshToken)) {
                // TODO 익셉션 처리해야함
                // TODO 익셉션 처리해야함
                // TODO 익셉션 처리해야함
                // TODO 익셉션 처리해야함
                // TODO 익셉션 처리해야함
                //ApiCmnResponse.error("400", "유효하지 않은 Refresh Token 입니다.");
                throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
            }

            String accessToken = authJwtTokenProvider.createToken(userName, Collections.singletonList(user.getRole()));
            accessTokenResponseMap.put("accessToken", accessToken);
            // === 현재시간과 Refresh Token 만료날짜를 통해 남은 만료기간 계산 === //
            // === Refresh Token 만료시간 계산해 1개월 미만일 시 refresh token도 발급 === //
            Date refreshExpiration = authJwtTokenProvider.getExpiration(refreshToken);
            long refreshExpireTime = refreshExpiration.getTime();
            long diffDays = (refreshExpireTime - now) / 1000 / (24 * 3600);
            long diffMin = (refreshExpireTime - now) / 1000 / 60;
            if (diffMin < 5) {
                String newRefreshToken = authJwtTokenProvider.createRefreshToken(userName);
                accessTokenResponseMap.put("refreshToken", newRefreshToken);
                user.setRefreshToken(refreshToken);
            }

        } catch (Exception e) {
            // 유효하지 않는 토큰입니다.
            // TODO 익셉션 처리해야함
            // TODO 익셉션 처리해야함
            // TODO 익셉션 처리해야함
            // TODO 익셉션 처리해야함
            // TODO 익셉션 처리해야함
            throw new RuntimeException(e);
        }
        return accessTokenResponseMap;
    }

}
