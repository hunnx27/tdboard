package com.twodollar.tdboard.modules.equipment.controller;

import com.twodollar.tdboard.modules.attach.service.EquipmentAttachService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.education.controller.response.EducationResponse;
import com.twodollar.tdboard.modules.equipment.controller.request.EquipmentRequest;
import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.service.EquipmentService;
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
public class EquipmentApiController {

    private final EquipmentService equipmentService;
    private final EquipmentAttachService equipmentAttachService;

    @Operation(summary = "장비 등록", description = "장비 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/equipments")
    public ResponseEntity<ApiCmnResponse<?>> equipmentDetail(
            @RequestBody(required = true)EquipmentRequest equipmentRequest
    ){
        try {
            Equipment equipmentEntity = equipmentService.createEquipment(equipmentRequest);
            if(equipmentRequest.getFiles()!=null && equipmentRequest.getFiles().size()>0){
                Long thumbFile = equipmentRequest.getFiles().get(0);
                equipmentAttachService.createAttach(equipmentEntity, thumbFile);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipmentEntity.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "장비 수정", description = "장비 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/equipments/{id}")
    public ResponseEntity<ApiCmnResponse<?>> equipmentUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) EquipmentRequest equipmentRequest
    ){
        try {
            Equipment equipment = equipmentService.updateEquipment(id, equipmentRequest);
            List<Long> thumbFiles = equipmentRequest.getFiles();
            equipmentAttachService.createUpdate(equipment.getId(), thumbFiles);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipment.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "장비 목록 조회", description = "장비 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/equipments")
    public ResponseEntity<ApiCmnResponse<?>> noticeAll(
            Pageable pageable
    ){
        try {
            long totalSize = equipmentService.getTotalEquipmentSize();
            List<Equipment> equipmentList = equipmentService.getEquipments(pageable);
            List<EquipmentResponse> equipmentResponseList = equipmentList.stream().map(equipment -> {
                EquipmentResponse equipmentResponse = equipment.toResponse();
                List<FileInfo> fileInfoes = equipmentAttachService.getAttaches(equipment.getId());
                List<FileInfoResponse> files = fileInfoes.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
                equipmentResponse.setFiles(files);
                if(files!=null && files.size()>0){
                    equipmentResponse.setImageUrl(files.get(0).getStoredPath());
                }
                return equipment.toResponse();
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(equipmentResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "장비 상세 조회", description = "장비 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/equipments/{id}")
    public ResponseEntity<ApiCmnResponse<?>> equipmentDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            Equipment equipment = equipmentService.getEquipmentById(id);
            EquipmentResponse equipmentResponse = equipment.toResponse();
            List<FileInfo> fileInfoes = equipmentAttachService.getAttaches(equipment.getId());
            List<FileInfoResponse> files = fileInfoes.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
            equipmentResponse.setFiles(files);
            if(files!=null && files.size()>0){
                equipmentResponse.setImageUrl(files.get(0).getStoredPath());
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipment.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "장비 삭제", description = "장비 삭제")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/equipments/{id}")
    public ResponseEntity<ApiCmnResponse<?>> equipmentDelete(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            equipmentService.deleteEquipmentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("deleted"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "시설에 포함된 장비 목록 조회", description = "시설에 포함된 장비 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/equipments/in-facility/{facilityId}")
    public ResponseEntity<ApiCmnResponse<?>> equipmentsInFacility(
            @PathVariable("facilityId") Long facilityId,
            Pageable pageable
    ){
        try {
            long totalSize = equipmentService.getTotalEquipmentSize(facilityId);
            List<Equipment> equipmentList = equipmentService.getEquipments(facilityId, pageable);
            List<EquipmentResponse> equipmentResponseList = equipmentList.stream().map(equipment -> {
                EquipmentResponse equipmentResponse = equipment.toResponse();
                List<FileInfo> fileInfoes = equipmentAttachService.getAttaches(equipment.getId());
                List<FileInfoResponse> files = fileInfoes.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
                equipmentResponse.setFiles(files);
                if(files!=null && files.size()>0){
                    equipmentResponse.setImageUrl(files.get(0).getStoredPath());
                }
                return equipmentResponse;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(equipmentResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
