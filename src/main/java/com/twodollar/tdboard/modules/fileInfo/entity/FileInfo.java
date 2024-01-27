package com.twodollar.tdboard.modules.fileInfo.entity;

import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import com.twodollar.tdboard.modules.fileInfo.util.dto.FileInfoDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String storedName;
    String storedPath;
    String originalName;
    long size;
    String type;
    String userId;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;


    public FileInfo(FileInfoDto fileInfoDto) {
        this.storedName = fileInfoDto.getStoredName();
        this.storedPath = fileInfoDto.getStoredPath();
        this.originalName = fileInfoDto.getOriginalName();
        this.size = fileInfoDto.getSize();
        this.type = fileInfoDto.getType();
    }

    public FileInfoResponse toResponse(){
        return FileInfoResponse.builder()
                .id(this.id)
                .storedName(this.storedName)
                .storedPath(this.storedPath)
                .originalName(this.originalName)
                .size(this.size)
                .type(this.type)
                .build();
    }


}
