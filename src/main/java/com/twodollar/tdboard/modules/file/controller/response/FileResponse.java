package com.twodollar.tdboard.modules.file.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
public class FileResponse {
    private Long id;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String fileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public FileResponse(Long id, String fileName, long fileSize, String fileType, String fileUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
