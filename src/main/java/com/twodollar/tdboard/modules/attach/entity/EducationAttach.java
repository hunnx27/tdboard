package com.twodollar.tdboard.modules.attach.entity;

import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.attach.entity.enums.AttachType;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
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
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class EducationAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="education_id", referencedColumnName = "id")
    private Education education;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="file_id", referencedColumnName = "id")
    private FileInfo fileInfo;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "comment 'THUMB, FILE' ")
    private AttachType attachType;
    @Column(columnDefinition = "INT(11) UNSIGNED' ")
    private int attachOrder;

    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    // 수정일
    private LocalDateTime updateAt;

    public EducationAttach(Education education, FileInfo fileInfo) {
        this.education = education;
        this.fileInfo = fileInfo;
        this.attachType = AttachType.THUMB;
        this.attachOrder = 0;
    }
}
