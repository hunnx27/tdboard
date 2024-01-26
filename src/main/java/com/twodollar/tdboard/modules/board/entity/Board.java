package com.twodollar.tdboard.modules.board.entity;

import com.twodollar.tdboard.modules.board.controller.response.BoardResponse;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
public class Board {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BoardTypeEnum boardType;
    // 제목
    private String title;
    // 내용
    private String context;
    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    // 답글시
    private long upId;
    // 조회 수
    @ColumnDefault("0")
    private long hit;
    @ColumnDefault("'N'")
    private String delYn;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    // 수정일
    private LocalDateTime updateAt;

    public BoardResponse toResponse() {
        return BoardResponse.builder()
            .id(this.id)
            .boardType(this.boardType.name())
            .title(this.title)
            .context(this.context)
            .userId(this.user.getId())
            .userIdName(this.user.getUserId())
            .userName(this.user.getUsername())
            .upId(this.upId)
            .hit(this.hit)
            .delYn(this.delYn)
            .createdAt(this.createdAt)
            .updatedAt(this.updateAt)
            .build();
    }
}
