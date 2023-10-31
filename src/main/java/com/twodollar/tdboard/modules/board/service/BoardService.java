package com.twodollar.tdboard.modules.board.service;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    /*
        게시판 공통
     */
    public List<Board> getBoards() {
        List<Board> todos = boardRepository.findAll();

        if(!todos.isEmpty()) return boardRepository.findAll();
        else throw new IllegalArgumentException("no such data");
    }
    public Board getBoardById(final Long id) {
        return boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Board createBoard(final Board createBoard) {
        if(createBoard == null) throw new IllegalArgumentException("todo item cannot be null");
        return boardRepository.save(createBoard);
    }

    public Board updateBoard(final long id, final Board updateBoard) {
        Board Board = getBoardById(id);
        Board.setTitle(updateBoard.getTitle());
        Board.setContext(updateBoard.getContext());
        return boardRepository.save(Board);
    }

    public void deleteBoardById(final Long id) {
        boardRepository.deleteById(id);
    }

    /*
        게시판 유형별 조회
     */
    public Page<Board> getNoticeBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.NOTICE, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getDataBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.DATA, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getFAQBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.FAQ, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getQNABoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.QNA, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        공지사항
     */
    public Page<Board> getNoticeBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.NOTICE, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getNoticeBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.NOTICE, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        자료실
     */
    public Page<Board> getDataBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.DATA, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getDataBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.DATA, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        FAQ
     */
    public Page<Board> getFAQBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.FAQ, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getFAQBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.FAQ, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        QNA
     */
    public Page<Board> getQNABoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.QNA, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public Page<Board> getQNABoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.QNA, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }




}
