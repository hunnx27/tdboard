package com.twodollar.tdboard.modules.facility.entity;

import com.twodollar.tdboard.modules.facility.controller.response.FacilityResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 시설명
    private String name;
    // 시설설명
    private String description;
    // imageUrl
    private String imageUrl;
    // 사용여부
    private String useYn;
    // 삭제여부
    private String delYn;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public FacilityResponse toResponse() {
        return FacilityResponse.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
