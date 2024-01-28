package com.twodollar.tdboard.modules.facility.controller;

import com.twodollar.tdboard.modules.attach.service.EducationAttachService;
import com.twodollar.tdboard.modules.attach.service.FacilityAttachService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.education.controller.response.EducationResponse;
import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.service.EquipmentService;
import com.twodollar.tdboard.modules.facility.controller.request.FacilityRequest;
import com.twodollar.tdboard.modules.facility.controller.response.FacilityResponse;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
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
public class FacilityApiController {

    private final FacilityService facilityService;
    private final FacilityAttachService facilityAttachService;

    @Operation(summary = "시설 등록", description = "시설 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/facilities")
    public ResponseEntity<ApiCmnResponse<?>> facilityDetail(
            @RequestBody(required = true) FacilityRequest facilityRequest
    ){
        try {
            Facility facilityEntity = facilityService.createFacility(facilityRequest);
            if(facilityRequest.getFiles()!=null && facilityRequest.getFiles().size()>0){
                Long thumbFile = facilityRequest.getFiles().get(0);
                facilityAttachService.createAttach(facilityEntity, thumbFile);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facilityEntity.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "시설 수정", description = "시설 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/facilities/{id}")
    public ResponseEntity<ApiCmnResponse<?>> facilityUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) FacilityRequest facilityRequest
    ){
        try {
            Facility facility = facilityService.updateFacility(id, facilityRequest);
            List<Long> thumbFiles = facilityRequest.getFiles();
            facilityAttachService.createUpdate(facility.getId(), thumbFiles);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facility.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "시설 목록 조회", description = "시설 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/facilities")
    public ResponseEntity<ApiCmnResponse<?>> noticeAll(
            Pageable pageable
    ){
        try {
            long totalSize = facilityService.getTotalFacilitySize();
            List<Facility> facilityList = facilityService.getFacilitys(pageable);
            List<FacilityResponse> facilityResponseList = facilityList.stream().map(facility -> {
                FacilityResponse facilityResponse = facility.toResponse();
                List<FileInfo> fileInfoes = facilityAttachService.getAttaches(facility.getId());
                List<FileInfoResponse> files = fileInfoes.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
                facilityResponse.setFiles(files);
                if(files!=null && files.size()>0){
                    facilityResponse.setImageUrl(files.get(0).getStoredPath());
                }
                return facilityResponse;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(facilityResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "시설 상세 조회", description = "시설 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/facilities/{id}")
    public ResponseEntity<ApiCmnResponse<?>> facilityDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            Facility facility = facilityService.getFacilityById(id);
            FacilityResponse facilityResponse = facility.toResponse();
            List<FileInfo> fileInfoes = facilityAttachService.getAttaches(facility.getId());
            List<FileInfoResponse> files = fileInfoes.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
            facilityResponse.setFiles(files);
            if(files!=null && files.size()>0){
                facilityResponse.setImageUrl(files.get(0).getStoredPath());
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(facilityResponse));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "시설 삭제", description = "시설 삭제")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/facilities/{id}")
    public ResponseEntity<ApiCmnResponse<?>> facilityDelete(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            facilityService.deleteFacilityById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("deleted"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }


}
