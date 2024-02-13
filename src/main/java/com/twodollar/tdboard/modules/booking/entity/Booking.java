package com.twodollar.tdboard.modules.booking.entity;

import com.twodollar.tdboard.modules.booking.controller.response.BookingResponse;
import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@DynamicInsert
@AllArgsConstructor
public class Booking {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 시설ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "comment 'NOTICE, QNA, DATA, FAQ' ")
    private BookingType bookingType; // 예약 구분
    private LocalDateTime startAt; // 예약 시작일시
    private LocalDateTime endAt; // 예약 종료일시
    private String reqPhone; // 신청 전화번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="approval_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User approvalUser; // 승인자
    @ColumnDefault("'N'")
    private String approvalYn; // 승인여부
    private LocalDateTime approvalAt; // 승인일시
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="education_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Education education;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="facility_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Facility facility;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="equipment_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="up_booking_id")
    private Booking upBooking;

    @Builder.Default
    @OneToMany(mappedBy = "upBooking", orphanRemoval = true)
    private List<Booking> relatedBooking = new ArrayList<>();

    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public BookingResponse toResponse() {
        BookingResponse bookingResponse = BookingResponse.builder()
                .id(this.id)
                .bookingType(this.bookingType.name())
                .startAt(this.startAt)
                .endAt(this.endAt)
                .reqPhone(this.reqPhone)
                .approvalYn(this.approvalYn)
                .approvalAt(this.approvalAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

        if(this.user!=null){
            bookingResponse.setUserId(this.user.getId());
            bookingResponse.setUserName(this.user.getUsername());
        }
        if(this.approvalUser!=null){
            bookingResponse.setApprovalUserId(this.approvalUser.getId());
            bookingResponse.setApprovalUserName(this.approvalUser.getUsername());
        }
        if(this.education!=null){
            bookingResponse.setEducationId(this.education.getId());
            bookingResponse.setEducationName(this.education.getName());
        }
        if(this.facility!=null){
            bookingResponse.setFacilityId(this.facility.getId());
            bookingResponse.setFacilityName(this.facility.getName());
        }
        if(this.equipment!=null){
            bookingResponse.setEquipmentId(this.equipment.getId());
            bookingResponse.setEquipmentName(this.equipment.getName());
        }

        return bookingResponse;
    }

    /**
     * 승인 처리
     * @param approvalUser
     */
    public void approval(User approvalUser){
        this.approvalUser = approvalUser;
        this.approvalYn = "Y";
        this.approvalAt = LocalDateTime.now();
    }

    /**
     * 승인 취소
     */
    public void cancel(){
        this.approvalYn = "N";
        this.approvalAt = LocalDateTime.now();
    }
}
