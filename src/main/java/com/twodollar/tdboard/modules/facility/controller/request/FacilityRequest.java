package com.twodollar.tdboard.modules.facility.controller.request;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FacilityRequest {
    private Long id;
    private String location;
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

    public Facility toEntity(){
        return Facility.builder().
                id(this.id).
                location(this.location).
                name(this.name).
                description(this.description).
                imageUrl(this.imageUrl).
                useYn(this.useYn).
                delYn(this.delYn).
                build();
    }
}
