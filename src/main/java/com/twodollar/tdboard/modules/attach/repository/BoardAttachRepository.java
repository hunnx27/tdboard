package com.twodollar.tdboard.modules.attach.repository;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.attach.entity.BoardAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardAttachRepository extends JpaRepository<BoardAttach, Long> {
    Optional<List<BoardAttach>> getBoardAttachesByBoard(Board board);

    void deleteBoardAttachesByBoard(Board board);


}
