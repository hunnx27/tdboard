package com.twodollar.tdboard.modules.education.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twodollar.tdboard.modules.education.controller.response.EducationResponse;
import com.twodollar.tdboard.modules.facility.entity.Facility;
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
public class Education {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="facility_id", referencedColumnName = "id")
    private Facility facility;

    // 시설명
    private String name;
    // 시설설명
    private String description;
    // imageUrl
    private String imageUrl;

    private String location;
    private String startDate; //강의시작일
    private String endDate; //강의종료일
    private String applicationStartDate;//접수시작일
    private String applicationEndDate; //접수종료일
    private String manager; // 교수명
    private int capacity; //정원

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

    public EducationResponse toResponse() {
        EducationResponse educationResponse = EducationResponse.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .location(this.location)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .applicationStartDate(this.applicationStartDate)
                .applicationEndDate(this.applicationEndDate)
                .manager(this.manager)
                .capacity(this.capacity)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

        if(this.facility!=null){
            educationResponse.setFacilityId(this.facility.getId());
            educationResponse.setFacilityName(this.facility.getName());
        }

        return educationResponse;
    }
}
