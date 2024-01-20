package com.twodollar.tdboard.modules.facility.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
public class FacilityResponse {
    private Long id;
    private String location;
    // 시설명
    private String name;
    // 시설설명
    private String description;
    // imageUrl
    private String imageUrl;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public FacilityResponse(Long id, String location, String name, String description, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
