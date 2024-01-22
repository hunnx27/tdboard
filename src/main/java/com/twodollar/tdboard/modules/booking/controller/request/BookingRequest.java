package com.twodollar.tdboard.modules.booking.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twodollar.tdboard.modules.booking.controller.response.BookingResponse;
import com.twodollar.tdboard.modules.booking.entity.Booking;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class BookingRequest {
    private Long id;
    private Long userId;
    private String bookingType; // 예약 구분
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt; // 예약 시작일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt; // 예약 종료일시
    private String reqPhone; // 신청 전화번호
    private Long approvalUserId; // 승인자
    private String approvalYn; // 승인여부
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime approvalAt; // 승인일시
    private Long educationId;
    private Long facilityId;
    private Long equipmentId;


    public Booking toEntity(){
        Booking booking = Booking.builder()
                .id(this.id)
                .bookingType(BookingType.valueOf(this.bookingType))
                .startAt(this.startAt)
                .endAt(this.endAt)
                .reqPhone(this.reqPhone)
                .approvalAt(this.approvalAt)
                .build();

        if(this.approvalYn!=null){
            booking.setApprovalYn(this.approvalYn);
        }

        return booking;
    }
}
