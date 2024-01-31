package com.twodollar.tdboard.modules.user.controller;

import com.twodollar.tdboard.modules.application.controller.request.ApplicationRequest;
import com.twodollar.tdboard.modules.application.controller.response.ApplicationResponse;
import com.twodollar.tdboard.modules.application.entity.Application;
import com.twodollar.tdboard.modules.application.service.ApplicationService;
import com.twodollar.tdboard.modules.booking.controller.request.BookingRequest;
import com.twodollar.tdboard.modules.booking.controller.response.BookingResponse;
import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.booking.service.BookingService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.controller.request.UserRequest;
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

@PreAuthorize("hasAnyRole('USER','PROFESSOR','ADMIN')")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserMeApiController {

    private final UserService userService;
    private final BookingService bookingService;
    private final ApplicationService applicationService;

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

    @Operation(summary = "사용자 예약 등록", description = "사용자 예약 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/users/me/bookings")
    public ResponseEntity<ApiCmnResponse<?>> bookingInsert(
            Authentication authentication,
            @RequestBody(required = true) BookingRequest bookingRequest
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            Booking bookingEntity = bookingService.createBooking(userId, bookingRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(bookingEntity.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "예약취소(시설/장비)(삭제)", description = "예약취소(시설/장비)(삭제)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/users/me/bookings/{id}")
    public ResponseEntity<ApiCmnResponse<?>> bookingDelete(
            Authentication authentication,
            @PathVariable("id") Long id
    ) throws Exception {
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            bookingService.deleteBookingById(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("deleted"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "사용자 예약 목록 조회", description = "사용자 예약 목록 조회(장비/시설)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/users/me/bookings")
    public ResponseEntity<ApiCmnResponse<?>> bookingAll(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(value = "bookingType", required = false) BookingType bookingType
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            User user = userService.getUserByUserId(userId);
            long totalSize = 0;
            List<Booking> bookingList = null;
            if(bookingType==null){
                totalSize = bookingService.getTotalBookingSize(user);
                bookingList = bookingService.getBookings(user, pageable);
            }else{
                totalSize = bookingService.getTotalBookingSize(user, bookingType);
                bookingList = bookingService.getBookings(user, bookingType, pageable);
            }
            List<BookingResponse> bookingResponseList = bookingList.stream().map(booking -> booking.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(bookingResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }


    @Operation(summary = "교육신청", description = "교육신청")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/users/me/applications")
    public ResponseEntity<ApiCmnResponse<?>> applicationInsert(
            Authentication authentication,
            @RequestBody(required = true) ApplicationRequest applicationRequest
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            Application applicationEntity = applicationService.createApplication(userId, applicationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(applicationEntity.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "교육취소(삭제)", description = "교육취소(삭제)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/users/me/applications/{id}")
    public ResponseEntity<ApiCmnResponse<?>> applicationDelete(
            Authentication authentication,
            @PathVariable("id") Long id
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            applicationService.deleteApplicationByIdAndUserId(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("deleted"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "사용자 교육 신청 목록 조회", description = "사용자 교육 신청 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/users/me/applications")
    public ResponseEntity<ApiCmnResponse<?>> applicationAll(
            Authentication authentication,
            Pageable pageable
    ){
        try {
            String userId = authentication.getName();
            if (userId == null || "".equals(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.");
            }
            long totalSize = applicationService.getTotalApplicationSize(userId);
            List<Application> applicationList = applicationService.getApplications(userId, pageable);
            List<ApplicationResponse> applicationResponseList = applicationList.stream().map(application -> application.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(applicationResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
