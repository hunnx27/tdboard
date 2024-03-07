package com.twodollar.tdboard.modules.booking.controller;

import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.booking.controller.request.BookingRequest;
import com.twodollar.tdboard.modules.booking.controller.response.BookingResponse;
import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.booking.service.BookingService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
//@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookingApiController {

    private final BookingService bookingService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "예약 승인", description = "예약 승인")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/bookings/{id}/approval")
    public ResponseEntity<ApiCmnResponse<?>> bookingApproval(
            Authentication authentication,
            @PathVariable("id") Long id
    ){
        try {
            String userId = authentication.getName();
            Booking booking = bookingService.approvalBooking(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(booking.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "예약 승인취소", description = "예약 승인취소")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiCmnResponse<?>> bookingCancel(
            Authentication authentication,
            @PathVariable("id") Long id
    ){
        try {
            String userId = authentication.getName();
            Booking booking = bookingService.cancelBooking(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(booking.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "예약 목록 조회", description = "예약 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/bookings")
    public ResponseEntity<ApiCmnResponse<?>> bookingAll(
            Pageable pageable,
            @RequestParam(value = "bookingType") BookingType bookingType
    ){
        try {
            long totalSize = bookingService.getTotalBookingSize();
            List<Booking> bookingList = bookingService.getBookings(bookingType, pageable);
            List<BookingResponse> bookingResponseList = bookingList.stream().map(booking -> booking.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(bookingResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "예약 상세 조회", description = "예약 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiCmnResponse<?>> bookingDetail(
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(booking.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "예약 가능 시간 조회(일자별)", description = "예약 가능 시간 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })

    @GetMapping("/bookings/bookingType/{bookingType}/target/{targetId}/available-times")
    public ResponseEntity<ApiCmnResponse<?>> bookingDate(
            @PathVariable(value = "bookingType") BookingType bookingType,
            @PathVariable("targetId") Long targetId,
            @RequestParam(value = "targetDate", required = false) String targetDate
    ){
        try {
            List<Integer> availableTimes = bookingService.getAvailableBookingTimes(bookingType, targetId, targetDate);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(availableTimes));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "예약현황(시설/장비)", description = "예약 현황 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name="bookingType", value="예약 타입(FACILITY:시설, EQUIPMENT:장비, EDUCATION:미사용)", required = true),
            @ApiImplicitParam(name="yearmonth", value="조회월(yyyy-mm)", required = false)
    })
    @GetMapping("/bookings/bookingType/{bookingType}/status")
    public ResponseEntity<ApiCmnResponse<?>> bookingStatus(
            @PathVariable(value = "bookingType") BookingType bookingType,
            @RequestParam(value = "yearmonth", required = false) String yearmonth
    ){
        try {
            List<Map<String,String>> list = bookingService.getBookingStatus(bookingType.name(), yearmonth);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(list));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

}
