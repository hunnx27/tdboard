package com.twodollar.tdboard.modules.facility.controller.response;

import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FacilityResponse {
    private Long id;
    // 시설명
    private String name;
    // 시설설명
    private String description;
    // imageUrl
    @Setter
    private String imageUrl;
    // 생성일
    @Setter
    private List<FileInfoResponse> files;
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public FacilityResponse(Long id, String name, String description, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
