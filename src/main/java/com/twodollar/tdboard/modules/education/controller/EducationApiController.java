package com.twodollar.tdboard.modules.education.controller;

import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.education.controller.request.EducationRequest;
import com.twodollar.tdboard.modules.education.controller.response.EducationResponse;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
//@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EducationApiController {

    private final EducationService educationService;

    @Operation(summary = "교육 등록", description = "교육 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/educations")
    public ResponseEntity<ApiCmnResponse<EducationResponse>> educationDetail(
            @RequestBody(required = true) EducationRequest educationRequest
    ){
        Education educationEntity = educationService.createEducation(educationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(educationEntity.toResponse()));
    }

    @Operation(summary = "교육 수정", description = "교육 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/educations/{id}")
    public ResponseEntity<ApiCmnResponse<EducationResponse>> educationUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) EducationRequest educationRequest
    ){
        Education education = educationService.updateEducation(id, educationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(education.toResponse()));
    }

    @Operation(summary = "교육 목록 조회", description = "교육 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/educations")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<EducationResponse>>> noticeAll(
            Pageable pageable
    ){
        long totalSize = educationService.getTotalEducationSize();
        List<Education> educationList = educationService.getEducations(pageable);
        List<EducationResponse> educationResponseList = educationList.stream().map(education -> education.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(educationResponseList, pageable, totalSize)));
    }

    @Operation(summary = "교육 상세 조회", description = "교육 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/educations/{id}")
    public ResponseEntity<ApiCmnResponse<EducationResponse>> educationDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        Education education = educationService.getEducationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(education.toResponse()));
    }

}