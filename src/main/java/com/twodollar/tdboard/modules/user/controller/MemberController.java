package com.twodollar.tdboard.modules.user.controller;

import com.twodollar.tdboard.modules.auth.service.AuthService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.controller.response.UserResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.entity.enums.RoleEnum;
import com.twodollar.tdboard.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * user 도메인(마이페이지) 와 겹쳐서 회원관리 쪽은 member 도메인으로 분리해놈..
 */
@PreAuthorize("hasAnyRole('ADMIN')")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
    private final UserService userService;
    private final AuthService authService;
    @Operation(summary = "회원 목록 조회(전체/권한별)", description = "회원 목록 조회(전체/권한별)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/members")
    public ResponseEntity<ApiCmnResponse<?>> memberList(
            @RequestParam(value = "role", required = false) RoleEnum role,
            Pageable pageable
    ){
        try {
            long totalSize = 0;
            List<User> userList = null;
            if(role == null){
                // 전체 조회
                totalSize = userService.getTotalUsersSize();
                userList = userService.getUsers(pageable);
            }else{
                // 권한별 조회
                totalSize = userService.getTotalUsersSizeByRole(role);
                userList = userService.getUsers(role, pageable);
            }
            List<UserResponse> applicationResponseList = userList.stream().map(user -> user.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(applicationResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "회원 상세 조회", description = "회원 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/members/{id}")
    public ResponseEntity<ApiCmnResponse<?>> memberDetail(
            @PathVariable("id") Long userId
    ){
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "회원 수정", description = "회원 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/members/{id}")
    public ResponseEntity<ApiCmnResponse<?>> memberModify(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest userRequest
    ){
        try {
            User user = userService.memberUpdate(userId, userRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "회원 비밀번호 초기화", description = "회원 비밀번호 초기화")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/members/{id}/reset-password")
    public ResponseEntity<ApiCmnResponse<?>> memberResetPassword(
            @PathVariable("id") Long userId
    ){
        try {
            User user = userService.getUserById(userId);
            user = authService.resetPassword(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.getPhone()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "회원 삭제(탈퇴)", description = "회원 삭제(탈퇴)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/members/{id}")
    public ResponseEntity<ApiCmnResponse<?>> memberDelete(
            @PathVariable("id") Long userId
    ){
        try {
            User user = userService.delete(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
