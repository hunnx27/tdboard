package com.twodollar.tdboard.modules.booking.controller.response;

import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
public class BookingResponse {
    private Long id;
    @Setter
    private Long userId;
    @Setter
    private String userName;
    private String bookingType; // 예약 구분
    private LocalDateTime startAt; // 예약 시작일시
    private LocalDateTime endAt; // 예약 종료일시
    private String reqPhone; // 신청 전화번호
    @Setter
    private Long approvalUserId;
    @Setter
    private String approvalUserName;
    private String approvalYn; // 승인여부
    private LocalDateTime approvalAt; // 승인일시
    @Setter
    private Long educationId;
    @Setter
    private String educationName;
    @Setter
    private Long facilityId;
    @Setter
    private String facilityName;
    @Setter
    private Long equipmentId;
    @Setter
    private String equipmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public BookingResponse(Long id, Long userId, String bookingType, LocalDateTime startAt, LocalDateTime endAt, String reqPhone, Long approvalUserId, String approvalYn, LocalDateTime approvalAt, Long educationId, Long facilityId, Long equipmentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.bookingType = bookingType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.reqPhone = reqPhone;
        this.approvalUserId = approvalUserId;
        this.approvalYn = approvalYn;
        this.approvalAt = approvalAt;
        this.educationId = educationId;
        this.facilityId = facilityId;
        this.equipmentId = equipmentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
