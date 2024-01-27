package com.twodollar.tdboard.modules.fileInfo.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileInfoResponse {
    private Long id;
    private String storedName;
    private String originalName;
    private long size;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public FileInfoResponse(Long id, String storedName, String originalName, long size, String type, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storedName = storedName;
        this.originalName = originalName;
        this.size = size;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
