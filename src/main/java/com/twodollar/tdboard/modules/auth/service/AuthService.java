package com.twodollar.tdboard.modules.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

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

    public Map<String, String> refresh(String refreshToken) throws ResponseStatusException {
        Map<String, String> accessTokenResponseMap = new HashMap<>();
        try {
            authJwtTokenProvider.validateToken(refreshToken, true);
            String userName = authJwtTokenProvider.getUserPk(refreshToken);
            User user = userRepository.findByUsername(userName).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 usernmae 입니다."));
            // === Access Token 재발급 === //
            long now = System.currentTimeMillis();
            if (!user.getRefreshToken().equals(refreshToken)) {
                //ApiCmnResponse.error("400", "유효하지 않은 Refresh Token 입니다.");
                throw new Exception("유효하지 않은 Refresh Token 입니다.");
            }

            String accessToken = authJwtTokenProvider.createToken(userName, Collections.singletonList(user.getRole()));
            accessTokenResponseMap.put("accessToken", accessToken);
            // === 현재시간과 Refresh Token 만료날짜를 통해 남은 만료기간 계산 === //
            // === Refresh Token 만료시간 계산해 1개월 미만일 시 refresh token도 발급 === //
            Date refreshExpiration = authJwtTokenProvider.getExpiration(refreshToken);
            long refreshExpireTime = refreshExpiration.getTime();
            long diffDays = (refreshExpireTime - now) / 1000 / (24 * 3600);
            long diffMin = (refreshExpireTime - now) / 1000 / 60;
            String newRefreshToken = null;
            if (diffMin < 5) {
                newRefreshToken = authJwtTokenProvider.createRefreshToken(userName);
                accessTokenResponseMap.put("refreshToken", newRefreshToken);
                user.setRefreshToken(refreshToken);
                userRepository.save(user);
            }
            accessTokenResponseMap.put("refreshToken", newRefreshToken);

        } catch (MalformedJwtException e) {
            String errorMsg = String.format("잘못된 JWT 서명입니다. message : %s", e.getMessage());
            log.info(errorMsg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg);
        } catch (ExpiredJwtException e) {
            String errorMsg = String.format("Access Token이 만료되었습니다. message : %s", e.getMessage());
            log.info("JwtAuthenticationFilter : {}", errorMsg);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMsg);
        } catch (UnsupportedJwtException e) {
            String errorMsg = String.format("JWT 토큰이 잘못되었습니다. message : %s", e.getMessage());
            log.info("JwtAuthenticationFilter : {}", errorMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        } catch (Exception e){
            String errorMsg = String.format("JWT 토큰이 잘못되었습니다. message : %s", e.getMessage());
            log.info("JwtAuthenticationFilter : {}", errorMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }
        return accessTokenResponseMap;
    }

}
