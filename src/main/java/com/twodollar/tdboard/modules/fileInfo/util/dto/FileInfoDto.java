package com.twodollar.tdboard.modules.fileInfo.util.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FileInfoDto {

    private String storedName;
    private String storedPath;
    private String originalName;
    private long size;
    private String type;

    @Builder
    public FileInfoDto(String storedName, String storedPath, String originalName, long size, String type) {
        this.storedName = storedName;
        this.storedPath = storedPath;
        this.originalName = originalName;
        this.size = size;
        this.type = type;
    }

}
