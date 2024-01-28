package com.twodollar.tdboard.modules.attach.entity;

import com.twodollar.tdboard.modules.equipment.entity.Equipment;
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
public class EquipmentAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="equipment_id", referencedColumnName = "id")
    private Equipment equipment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="file_id", referencedColumnName = "id")
    private FileInfo fileInfo;
    @Enumerated(EnumType.STRING)
    private AttachType attachType;
    private int attachOrder;

    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    // 수정일
    private LocalDateTime updateAt;

    public EquipmentAttach(Equipment equipment, FileInfo fileInfo) {
        this.equipment = equipment;
        this.fileInfo = fileInfo;
        this.attachType = AttachType.FILE;
        this.attachOrder = 0;
    }
}