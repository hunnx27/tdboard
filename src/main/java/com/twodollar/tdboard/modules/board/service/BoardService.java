package com.twodollar.tdboard.modules.board.service;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
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
    public List<Board> getNoticeBoards() {
        return boardRepository.findByBoardType(BoardTypeEnum.NOTICE).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getDataBoards() {
        return boardRepository.findByBoardType(BoardTypeEnum.DATA).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getFAQBoards() {
        return boardRepository.findByBoardType(BoardTypeEnum.FAQ).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getQNABoards() {
        return boardRepository.findByBoardType(BoardTypeEnum.QNA).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        공지사항
     */
    public List<Board> getNoticeBoardsWithTitle(String title) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.NOTICE, title).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getNoticeBoardsWithContext(String context) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.NOTICE, context).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        자료실
     */
    public List<Board> getDataBoardsWithTitle(String title) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.DATA, title).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getDataBoardsWithContext(String context) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.DATA, context).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        FAQ
     */
    public List<Board> getFAQBoardsWithTitle(String title) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.FAQ, title).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getFAQBoardsWithContext(String context) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.FAQ, context).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        QNA
     */
    public List<Board> getQNABoardsWithTitle(String title) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.QNA, title).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getQNABoardsWithContext(String context) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.QNA, context).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }




}
