package com.twodollar.tdboard.modules.attach.entity;

import com.twodollar.tdboard.modules.facility.entity.Facility;
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
public class FacilityAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="facility_id", referencedColumnName = "id")
    private Facility facility;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="file_id", referencedColumnName = "id")
    private FileInfo fileInfo;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) DEFAULT NULL comment 'THUMB, FILE' ")
    private AttachType attachType;
    @Column(columnDefinition = "INT(11) UNSIGNED' ")
    private int attachOrder;

    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    // 수정일
    private LocalDateTime updateAt;

    public FacilityAttach(Facility facility, FileInfo fileInfo) {
        this.facility = facility;
        this.fileInfo = fileInfo;
        this.attachType = AttachType.FILE;
        this.attachOrder = 0;
    }
}
