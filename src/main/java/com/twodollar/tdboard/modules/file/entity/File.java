package com.twodollar.tdboard.modules.file.entity;

import com.twodollar.tdboard.modules.file.controller.response.FileResponse;
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
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String fileName;
    long fileSize;
    String fileType;
    String fileUrl;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '최종 변경 일자' ")
    private LocalDateTime updatedAt;

    public FileResponse toResponse(){
        return FileResponse.builder()
                .id(this.id)
                .fileName(this.fileName)
                .fileSize(this.fileSize)
                .fileType(this.fileType)
                .fileUrl(this.fileUrl)
                .build();
    }
}
