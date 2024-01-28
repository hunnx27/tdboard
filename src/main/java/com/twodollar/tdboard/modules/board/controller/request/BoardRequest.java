package com.twodollar.tdboard.modules.board.controller.request;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardRequest {
    private Long id;
    private String boardType;
    private String title;
    private String context;
    @Setter
    private long upId;

    private List<Long> files;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .boardType(BoardTypeEnum.valueOf(boardType))
                .title(title)
                .context(context)
                .upId(upId)
                .build();
    }
}
