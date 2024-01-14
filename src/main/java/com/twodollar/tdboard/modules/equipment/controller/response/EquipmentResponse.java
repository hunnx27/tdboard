package com.twodollar.tdboard.modules.equipment.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EquipmentResponse {
    private Long id;

    private String boardType;

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
    private LocalDateTime createdDate;

    // 게시글 수정일
    private LocalDateTime modifiedDate;

    @Builder
    public EquipmentResponse(Long id, String boardType, String title, String context, long userId, long upId, long hit, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.boardType = boardType;
        this.title = title;
        this.context = context;
        this.userId = userId;
        this.upId = upId;
        this.hit = hit;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
