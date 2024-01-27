package com.twodollar.tdboard.modules.board.controller.response;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse {
    private Long id;

    private String boardType;

    // 제목
    private String title;

    // 내용
    private String context;

    // 사용자
    private long userId;
    private String userIdName;
    private String userName;

    // 답글시
    private long upId;

    // 조회 수
    private long hit;

    private String delYn;

    // 게시글 생성일
    private LocalDateTime createdAt;

    // 게시글 수정일
    private LocalDateTime updatedAt;

    private BoardResponse upBoard;

    @Setter
    private List<FileInfoResponse> files;

    @Builder
    public BoardResponse(Long id, String boardType, String title, String context, long userId, String userIdName, String userName, long upId, long hit, String delYn, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.boardType = boardType;
        this.title = title;
        this.context = context;
        this.userId = userId;
        this.userIdName = userIdName;
        this.userName = userName;
        this.upId = upId;
        this.hit = hit;
        this.delYn = delYn;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
