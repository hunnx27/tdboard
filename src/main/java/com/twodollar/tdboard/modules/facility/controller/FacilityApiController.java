package com.twodollar.tdboard.modules.facility.controller;

import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.facility.controller.request.FacilityRequest;
import com.twodollar.tdboard.modules.facility.controller.response.FacilityResponse;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
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
public class FacilityApiController {

    private final FacilityService facilityService;

    @Operation(summary = "시설 등록", description = "시설 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/facilities")
    public ResponseEntity<ApiCmnResponse<FacilityResponse>> facilityDetail(
            @RequestBody(required = true) FacilityRequest facilityRequest
    ){
        Facility facilityEntity = facilityService.createFacility(facilityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facilityEntity.toResponse()));
    }

    @Operation(summary = "시설 수정", description = "시설 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/facilities/{id}")
    public ResponseEntity<ApiCmnResponse<FacilityResponse>> facilityUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) FacilityRequest facilityRequest
    ){
        Facility facility = facilityService.updateFacility(id, facilityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facility.toResponse()));
    }

    @Operation(summary = "시설 목록 조회", description = "시설 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/facilities")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<FacilityResponse>>> noticeAll(
            Pageable pageable
    ){
        long totalSize = facilityService.getTotalFacilitySize();
        List<Facility> facilityList = facilityService.getFacilitys(pageable);
        List<FacilityResponse> facilityResponseList = facilityList.stream().map(facility -> facility.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(facilityResponseList, pageable, totalSize)));
    }

    @Operation(summary = "시설 상세 조회", description = "시설 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/facilities/{id}")
    public ResponseEntity<ApiCmnResponse<FacilityResponse>> facilityDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        Facility facility = facilityService.getFacilityById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facility.toResponse()));
    }

}
