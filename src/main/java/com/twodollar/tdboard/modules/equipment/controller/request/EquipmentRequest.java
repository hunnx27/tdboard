package com.twodollar.tdboard.modules.equipment.controller.request;

import com.twodollar.tdboard.modules.equipment.controller.response.EquipmentResponse;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
public class EquipmentRequest {
    private Long id;
    private Long facilityId;
    private String location;
    // 장비명
    private String name;
    // 장비설명
    private String description;
    private List<Long> files;
    // 사용여부
    private String useYn;
    // 삭제여부
    private String delYn;

    public Equipment toEntity(){
        return Equipment.builder().
                id(this.id).
                location(this.location).
                name(this.name).
                description(this.description).
                useYn(this.useYn).
                delYn(this.delYn).
                build();
    }
}
