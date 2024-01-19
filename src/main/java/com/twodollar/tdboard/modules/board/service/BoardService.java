package com.twodollar.tdboard.modules.board.service;
import com.twodollar.tdboard.modules.board.controller.request.BoardRequest;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.board.repository.BoardRepository;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    /*
        공통
     */
    public Board getBoardById(final Long id, BoardTypeEnum boardTypeEnum) throws ResponseStatusException{
        return boardRepository.findByBoardTypeAndId(boardTypeEnum, id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 글이 없습니다."));
    }
    public int getTotalBoardSize(BoardTypeEnum boardTypeEnum){
        return boardRepository.countByBoardType(boardTypeEnum);
    }
    public Board createBoard(final BoardRequest createBoard) throws ResponseStatusException{
        if(createBoard == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "글을 등록할 수 없습니다. 요청 내용을 확인하세요.");
        return boardRepository.save(createBoard.toEntity());
    }

    /**
     * 특정 게시글의 답글 갯수
     * @param upId
     * @return
     */
    public int getTotalReplyBoardSize(Long upId){
        return boardRepository.countByUpId(upId);
    }

    /**
     * 답글 목록 조회
     * @param upId
     * @param pageable
     * @return
     */
    public List<Board> getBoardsByUpId(final Long upId, Pageable pageable){
        return boardRepository.findByUpId(upId, pageable).orElse(null);
    }

    public Board updateBoard(final long id, final BoardRequest updateBoard) throws ResponseStatusException{
        Board board = this.getBoardById(id, BoardTypeEnum.valueOf(updateBoard.getBoardType()));
        board.setTitle(updateBoard.getTitle());
        board.setContext(updateBoard.getContext());
        return boardRepository.save(board);
    }

    public Board deleteBoard(final Long id) throws ResponseStatusException{
        Board board = boardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 글이 없습니다."));
        board.setDelYn("Y");
        return boardRepository.save(board);
    }

    public Board upHitBoard(final Long id) throws ResponseStatusException{
        Board board = boardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 글이 없습니다."));
        board.setHit(board.getHit()+1);
        return boardRepository.save(board);
    }

    /*
        게시판 유형별 조회
     */
    public List<Board> getNoticeBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.NOTICE, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getDataBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.DATA, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getFAQBoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.FAQ, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getQNABoards(Pageable pageable) {
        return boardRepository.findByBoardType(BoardTypeEnum.QNA, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        공지사항
     */
    public List<Board> getNoticeBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.NOTICE, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getNoticeBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.NOTICE, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        자료실
     */
    public List<Board> getDataBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.DATA, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getDataBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.DATA, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        FAQ
     */
    public List<Board> getFAQBoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.FAQ, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getFAQBoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.FAQ, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    /*
        QNA
     */
    public List<Board> getQNABoardsWithTitle(String title, Pageable pageable) {
        return boardRepository.findByBoardTypeAndTitleContains(BoardTypeEnum.QNA, title, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }
    public List<Board> getQNABoardsWithContext(String context, Pageable pageable) {
        return boardRepository.findByBoardTypeAndContextContains(BoardTypeEnum.QNA, context, pageable).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }




}
