package com.twodollar.tdboard.modules.board.repository;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // username을 가지고 User 정보를 가져올 수 있게 메소드 생성
    Optional<List<Board>> findByBoardType(BoardTypeEnum boardTypeEnum, Pageable pageable);
    Optional<List<Board>> findByBoardTypeAndTitleContains(BoardTypeEnum boardTypeEnum, String title, Pageable pageable);
    Optional<List<Board>> findByBoardTypeAndContextContains(BoardTypeEnum boardTypeEnum, String context, Pageable pageable);
}
