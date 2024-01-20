package com.twodollar.tdboard.modules.education.controller.request;

import com.twodollar.tdboard.modules.education.entity.Education;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class EducationRequest {
    private Long id;
    // 교육명
    private String name;
    // 교육설명
    private String description;
    // imageUrl
    private String imageUrl;
    private Long facilityId;
    private String location;
    private String startDate; //강의시작일
    private String endDate; //강의종료일
    private String applicationStartDate;//접수시작일
    private String applicationEndDate; //접수종료일
    private String manager; // 교수명
    private int capicity; //정원
    // 사용여부
    private String useYn;
    // 삭제여부
    private String delYn;

    public Education toEntity(){
        return Education.builder().
                id(this.id).
                name(this.name).
                description(this.description).
                imageUrl(this.imageUrl).
                location(this.location).
                startDate(this.startDate).
                endDate(this.endDate).
                applicationStartDate(this.applicationStartDate).
                applicationEndDate(this.applicationEndDate).
                manager(this.manager).
                capicity(this.capicity).
                useYn(this.useYn).
                delYn(this.delYn).
                build();
    }
}
