package com.twodollar.tdboard.modules.facility.controller.request;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class FacilityRequest {
    private Long id;
    // 시설명
    private String name;
    // 시설설명
    private String description;
    // imageUrl
    private String imageUrl;
    private List<Long> files;
    // 사용여부
    private String useYn;
    // 삭제여부
    private String delYn;

    public Facility toEntity(){
        return Facility.builder().
                id(this.id).
                name(this.name).
                description(this.description).
                imageUrl(this.imageUrl).
                useYn(this.useYn).
                delYn(this.delYn).
                build();
    }
}
