package com.twodollar.tdboard.modules.application.controller.response;

import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
public class ApplicationResponse {
    private Long id;
    @Setter
    private Long userId;
    @Setter
    private String userName;
    private String reqPhone;
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ApplicationResponse(Long id, Long userId, String userName, String reqPhone, Long approvalUserId, String approvalUserName, String approvalYn, LocalDateTime approvalAt, Long educationId, String educationName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.reqPhone = reqPhone;
        this.approvalUserId = approvalUserId;
        this.approvalUserName = approvalUserName;
        this.approvalYn = approvalYn;
        this.approvalAt = approvalAt;
        this.educationId = educationId;
        this.educationName = educationName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
