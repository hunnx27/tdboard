package com.twodollar.tdboard.modules.user.controller;

import com.twodollar.tdboard.modules.board.controller.response.BoardResponse;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
import com.twodollar.tdboard.modules.user.controller.response.UserResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasAnyRole('USER','PROFESSION','ADMIN')")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    @Operation(summary = "사용자 상세 조회", description = "사용자 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/users/me")
    public ResponseEntity<ApiCmnResponse<?>> userDetailMe(
            Authentication authentication
    ){
        String userId = authentication.getName();
        if(userId == null || "".equals(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiCmnResponse.error("404", "등록된 사용자가 없습니다."));
        }

        try {
            User user = userService.getUserByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "사용자 수정", description = "사용자 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/users/me")
    public ResponseEntity<ApiCmnResponse<?>> userModifyMe(
            Authentication authentication,
            @RequestBody UserRequest userRequest
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            User user = userService.update(userId, userRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(user.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
