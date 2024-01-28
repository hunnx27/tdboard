package com.twodollar.tdboard.modules.education.controller.response;

import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EducationResponse {
    private Long id;
    // 시설명
    private String name;
    // 시설설명
    private String description;
    @Setter
    private String imageUrl;
    @Setter
    private Long facilityId;
    @Setter
    private String facilityName;

    private String location;
    private String startDate; //강의시작일
    private String endDate; //강의종료일
    private String applicationStartDate;//접수시작일
    private String applicationEndDate; //접수종료일
    private String manager; // 교수명
    private int capacity; //정원

    @Setter
    private List<FileInfoResponse> files;

    // 생성일
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public EducationResponse(Long id, String name, String description, String imageUrl, Long facilityId, String facilityName, String location, String startDate, String endDate, String applicationStartDate, String applicationEndDate, String manager, int capacity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.manager = manager;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
