package com.twodollar.tdboard.modules.application.entity;

import com.twodollar.tdboard.modules.application.controller.response.ApplicationResponse;
import com.twodollar.tdboard.modules.booking.controller.response.BookingResponse;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Audited // 이력생성
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Application {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="education_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Education education;
    private String reqPhone;
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="approval_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User approvalUser; // 승인자
    @ColumnDefault("'N'")
    private String approvalYn; // 승인여부
    private LocalDateTime approvalAt; // 승인일시

    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public ApplicationResponse toResponse() {
        ApplicationResponse applicationResponse = ApplicationResponse.builder()
                .id(this.id)
                .reqPhone(this.reqPhone)
                .approvalYn(this.approvalYn)
                .approvalAt(this.approvalAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

        if(this.user!=null){
            applicationResponse.setUserId(this.user.getId());
            applicationResponse.setUserName(this.user.getUsername());
        }
        if(this.approvalUser!=null){
            applicationResponse.setApprovalUserId(this.approvalUser.getId());
            applicationResponse.setApprovalUserName(this.approvalUser.getUsername());
        }
        if(this.education!=null){
            applicationResponse.setEducationId(this.education.getId());
            applicationResponse.setEducationName(this.education.getName());
        }
        return applicationResponse;
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
     * @param approvalUser
     */
    public void cancel(){
        this.approvalYn = "N";
        this.approvalAt = LocalDateTime.now();
    }
}
