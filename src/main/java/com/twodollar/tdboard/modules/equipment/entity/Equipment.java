package com.twodollar.tdboard.modules.equipment.entity;

import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Equipment {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 시설ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="facility_id", referencedColumnName = "id")
    private Facility facility;
    // 대여 장소
    private String location;
    // 장비명
    private String name;
    // 장비설명
    @Column(columnDefinition = "TEXT")
    private String description;
    // imageUrl
    private String imageUrl;
    // 사용여부
    @ColumnDefault("'Y'")
    private String useYn;
    // 삭제여부
    @ColumnDefault("'N'")
    private String delYn;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public EquipmentResponse toResponse() {
        EquipmentResponse equipmentResponse = EquipmentResponse.builder()
                .id(this.id)
                .location(this.location)
                .name(this.name)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

        if(this.facility!=null){
            equipmentResponse.setFacilityId(this.facility.getId());
            equipmentResponse.setFacilityName(this.facility.getName());
        }

        return equipmentResponse;
    }
}
