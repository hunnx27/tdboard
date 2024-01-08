package com.twodollar.tdboard.modules.board.entity;

import com.twodollar.tdboard.modules.board.controller.response.BoardResponse;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
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

    // 이메일
    private long userId;

    // 답글시
    private long upId;

    // 조회 수
    private long hit;

    // 게시글 생성일
    @CreationTimestamp
    private LocalDateTime createdDate;

    // 게시글 수정일
    private LocalDateTime modifiedDate;

    public BoardResponse toResponse() {
        return BoardResponse.builder()
            .id(this.id)
            .boardType(this.boardType.name())
            .title(this.title)
            .context(this.context)
            .userId(this.userId)
            .upId(this.upId)
            .hit(this.hit)
            .createdDate(this.createdDate)
            .modifiedDate(this.modifiedDate)
            .build();
    }
}
