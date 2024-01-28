package com.twodollar.tdboard.modules.equipment.controller.response;

import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EquipmentResponse {
    private Long id;
    @Setter
    private Long facilityId;
    @Setter
    private String facilityName;
    private String location;
    // 장비명
    private String name;
    // 장비설명
    private String description;
    // imageUrl
    @Setter
    private String imageUrl;
    @Setter
    private List<FileInfoResponse> files;
    // 생성일
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public EquipmentResponse(Long id, Long facilityId, String facilityName, String location, String name, String description, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.location = location;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
