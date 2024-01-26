package com.twodollar.tdboard.modules.file.controller.request;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.file.entity.File;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class FileRequest {
    private Long id;
    private String fileName;
    private long fileSize;
    private String fileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public File toEntity(){
        return File.builder().
                id(this.id).
                fileName(this.fileName).
                fileSize(this.fileSize).
                fileUrl(this.fileUrl).
                createdAt(this.createdAt).
                updatedAt(this.updatedAt).
                build();
    }
}
