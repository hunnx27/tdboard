package com.twodollar.tdboard.modules.survey.entity;

import com.twodollar.tdboard.modules.survey.controller.response.SurveyResponse;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surveyUrl;
    @ColumnDefault("'Y'")
    private String useYn;
    @ColumnDefault("'N'")
    private String delYn;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public SurveyResponse toResponse() {
        return SurveyResponse.builder()
                .id(this.id)
                .name(this.name)
                .surveyUrl(this.surveyUrl)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
