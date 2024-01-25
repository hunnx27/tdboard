package com.twodollar.tdboard.modules.application.controller;

import com.twodollar.tdboard.modules.application.controller.request.ApplicationRequest;
import com.twodollar.tdboard.modules.application.controller.response.ApplicationResponse;
import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.application.service.ApplicationService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
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

@Slf4j
@RestController
//@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApplicationApiController {

    private final ApplicationService applicationService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "교육신청 승인", description = "교육신청 승인")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/applications/{id}/approval")
    public ResponseEntity<ApiCmnResponse<?>> applicationApproval(
            Authentication authentication,
            @PathVariable("id") Long id
    ){
        try {
            String userId = authentication.getName();
            Application application = applicationService.approvalApplication(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(application.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "교육신청 승인취소", description = "교육신청 승인취소")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/applications/{id}/cancel")
    public ResponseEntity<ApiCmnResponse<?>> applicationCancel(
            Authentication authentication,
            @PathVariable("id") Long id
            ){
        try {
            String userId = authentication.getName();
            Application application = applicationService.cancelApplication(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(application.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "교육신청 목록 조회", description = "교육신청 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/applications")
    public ResponseEntity<ApiCmnResponse<?>> noticeAll(
            Pageable pageable
    ){
        try {
            long totalSize = applicationService.getTotalApplicationSize();
            List<Application> applicationList = applicationService.getApplications(pageable);
            List<ApplicationResponse> applicationResponseList = applicationList.stream().map(application -> application.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(applicationResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "교육신청 상세 조회", description = "교육신청 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/applications/{id}")
    public ResponseEntity<ApiCmnResponse<?>> applicationDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            Application application = applicationService.getApplicationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(application.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

}
