package com.twodollar.tdboard.modules.auth.controller;

import com.twodollar.tdboard.modules.auth.controller.request.UserAuthRequest;
import com.twodollar.tdboard.modules.auth.controller.request.UserFindRequest;
import com.twodollar.tdboard.modules.auth.controller.request.UserPasswordRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PostMapping("/auth/refresh")
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

    /**
     * ID 중복확인 API
     * @param userId
     * @return
     */
    @Operation(summary = "ID 중복확인", description = "ID 중복확인")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/user-ids/{userId}/exists")
    public ResponseEntity<ApiCmnResponse<?>> existsUserId(
            @PathVariable("userId") String userId
    ){
        try{
            int cnt = userRepository.countByUserId(userId);
            if(cnt!=0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 id입니다.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("unique"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    /**
     * email 중복확인 API
     * @param email
     * @return
     */
    @Operation(summary = "email 중복확인", description = "email 중복확인")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/user-emails/{email}/exists")
    public ResponseEntity<ApiCmnResponse<?>> existsEmail(
            @PathVariable("email") String email
    ){
        try{
            int cnt = userRepository.countByEmail(email);
            if(cnt!=0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 email입니다.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("unique"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    /**
     * email ID찾기 API
     * @param userFindRequest
     * @return
     */
    @Operation(summary = "ID 찾기", description = "ID 찾기")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/find/id")
    public ResponseEntity<ApiCmnResponse<?>> findId(
            @RequestBody UserFindRequest userFindRequest
    ){
        try{
            String username = userFindRequest.getUsername();
            String userEmail = userFindRequest.getEmail();
            User user = userRepository.findByUsernameAndEmail(username, userEmail).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 사용자가 없습니다."));
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.getUserId()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    /**
     * email 패스워드 찾기 API
     * @param userFindRequest
     * @return
     */
    @Operation(summary = "패스워드 찾기", description = "패스워드 찾기")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/find/password")
    public ResponseEntity<ApiCmnResponse<?>> findPassword(
            @RequestBody UserFindRequest userFindRequest
    ){
        try{
            String username = userFindRequest.getUsername();
            String userId = userFindRequest.getUserId();
            String userEmail = userFindRequest.getEmail();
            User user = userRepository.findByUsernameAndUserIdAndEmail(username, userId, userEmail).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 사용자가 없습니다."));

            user = authService.resetPassword(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.getPhone()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyRole('USER','PROFESSOR','ADMIN')")
    /**
     * email 본인 인증(개인정보 수정 시)
     * @param userPasswordRequest
     * @return
     */
    @Operation(summary = "본인 인증", description = "본인 인증(개인정보 수정 시, 탈퇴 시)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/validate/me")
    public ResponseEntity<ApiCmnResponse<?>> validMe(
            Authentication authentication,
            @RequestBody UserPasswordRequest userPasswordRequest

            ){
        try{
            String userId = authentication.getName();
            String password = userPasswordRequest.getPassword();

            User user = authService.validUser(userId, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('USER','PROFESSOR','ADMIN')")
    /**
     * email 회원 탈퇴
     * @param userPasswordRequest
     * @return
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴(탈퇴 시)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/auth/sucession/me")
    public ResponseEntity<ApiCmnResponse<?>> dropMe(
            Authentication authentication,
            @RequestBody UserPasswordRequest userPasswordRequest
    ){
        try{
            String userId = authentication.getName();
            String password = userPasswordRequest.getPassword();

            User user = authService.validUser(userId, password);
            user.sucession();
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
