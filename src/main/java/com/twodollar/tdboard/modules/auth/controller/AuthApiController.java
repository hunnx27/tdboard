package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.controller.request.UserAuthRequest;
import com.twodollar.tdboard.modules.auth.service.AuthJwtTokenProvider;
import com.twodollar.tdboard.modules.auth.service.AuthService;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.controller.response.UserResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/api/v1/")
public class AuthApiController {
    private final AuthJwtTokenProvider authJwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthService authService;

    /**
     * 로그인 API(테스트 용, 실제로는 시큐리티 폼로그인 방식으로 사용함)
     * @param userMap
     * @return
     */
    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiCmnResponse<?>> login(@RequestBody Map<String, String> userMap) {
        String userId = userMap.get("userId");
        log.info("user id = {}", userId);
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다."));
            String accessToken = authJwtTokenProvider.createToken(userId, Collections.singletonList(user.getRole().name()));
            String refreshToken = authJwtTokenProvider.createRefreshToken(userId);
            // Refresh Token DB에 저장
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            Map<String, String> newTokens = new HashMap<>();
            newTokens.put("accessToken", accessToken);
            newTokens.put("refreshToken", refreshToken);
            return ResponseEntity.ok(ApiCmnResponse.success(newTokens));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    /**
     * 리프레시 토큰 발급 API
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/auth/refresh")
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
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }
    }

    /**
     * 회원 가입 API
     * @param userAuthRequest
     * @return
     */
    @Operation(summary = "회원 가입", description = "사용자 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/join")
    public ResponseEntity<ApiCmnResponse<?>> join(
            @RequestBody UserAuthRequest userAuthRequest
    ){
        try{
            User user = authService.join(userAuthRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }

    }
}
