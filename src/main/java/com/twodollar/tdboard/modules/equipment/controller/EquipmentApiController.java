package com.twodollar.tdboard.modules.equipment.controller;

import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.equipment.controller.request.EquipmentRequest;
import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.service.EquipmentService;
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
public class EquipmentApiController {

    private final EquipmentService equipmentService;

    @Operation(summary = "장비 등록", description = "장비 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/equipments")
    public ResponseEntity<ApiCmnResponse<EquipmentResponse>> equipmentDetail(
            @RequestBody(required = true)EquipmentRequest equipmentRequest
    ){
        Equipment equipmentEntity = equipmentService.createEquipment(equipmentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipmentEntity.toResponse()));
    }

    @Operation(summary = "장비 수정", description = "장비 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/equipments/{id}")
    public ResponseEntity<ApiCmnResponse<EquipmentResponse>> equipmentUpdate(
            @PathVariable("id") Long id,
            @RequestBody(required = true) EquipmentRequest equipmentRequest
    ){
        Equipment equipment = equipmentService.updateEquipment(id, equipmentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipment.toResponse()));
    }

    @Operation(summary = "장비 목록 조회", description = "장비 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/equipments")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<EquipmentResponse>>> noticeAll(
            Pageable pageable
    ){
        long totalSize = equipmentService.getTotalEquipmentSize();
        List<Equipment> equipmentList = equipmentService.getEquipments(pageable);
        List<EquipmentResponse> equipmentResponseList = equipmentList.stream().map(equipment -> equipment.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(equipmentResponseList, pageable, totalSize)));
    }

    @Operation(summary = "장비 상세 조회", description = "장비 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/equipments/{id}")
    public ResponseEntity<ApiCmnResponse<EquipmentResponse>> equipmentDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(equipment.toResponse()));
    }

}
