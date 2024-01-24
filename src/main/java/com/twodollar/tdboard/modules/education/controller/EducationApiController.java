package com.twodollar.tdboard.modules.education.controller;

import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.education.controller.request.EducationRequest;
import com.twodollar.tdboard.modules.education.controller.response.EducationResponse;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.service.EducationService;
import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<ApiCmnResponse<?>> educationDetail(
            @RequestBody(required = true) EducationRequest educationRequest
    ){
        try {
            Education educationEntity = educationService.createEducation(educationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(educationEntity.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육 수정", description = "교육 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/educations/{id}")
    public ResponseEntity<ApiCmnResponse<?>> educationUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) EducationRequest educationRequest
    ){
        try {
            Education education = educationService.updateEducation(id, educationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(education.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육 목록 조회", description = "교육 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/educations")
    public ResponseEntity<ApiCmnResponse<?>> noticeAll(
            Pageable pageable
    ){
        try {
            long totalSize = educationService.getTotalEducationSize();
            List<Education> educationList = educationService.getEducations(pageable);
            List<EducationResponse> educationResponseList = educationList.stream().map(education -> education.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(educationResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육 목록 조회(캘린더)", description = "교육 목록 조회(캘린더)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/educations/calendar/year/{year}/month/{month}")
    public ResponseEntity<ApiCmnResponse<?>> educationCalendar(
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ){
        try {
            List<Education> educationList = educationService.getEducationsYearMonth(year, month);
            List<EducationResponse> educationResponseList = educationList.stream().map(education -> education.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(educationResponseList));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육 상세 조회", description = "교육 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/educations/{id}")
    public ResponseEntity<ApiCmnResponse<?>> educationDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            Education education = educationService.getEducationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(education.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육 삭제", description = "교육 삭제")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/educations/{id}")
    public ResponseEntity<ApiCmnResponse<?>> educationDelete(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            educationService.deleteEducationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("deleted"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

}
