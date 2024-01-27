package com.twodollar.tdboard.modules.fileInfo.service;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.repository.BoardRepository;
import com.twodollar.tdboard.modules.facility.controller.request.FacilityRequest;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.fileInfo.entity.BoardAttach;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.entity.enums.AttachType;
import com.twodollar.tdboard.modules.fileInfo.repository.BoardAttachRepository;
import com.twodollar.tdboard.modules.fileInfo.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachService {
    private final BoardAttachRepository boardAttachRepository;
    private final BoardRepository boardRepository;
    private final FileInfoRepository fileInfoRepository;
    public List<FileInfo> getAttaches(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 데이터가 없습니다."));
        List<BoardAttach> list = boardAttachRepository.getBoardAttachesByBoard(board).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 첨부파일이 없습니다.")));

        List<FileInfo> fileList = list.stream().map(boardAttach -> boardAttach.getFileInfo()).collect(Collectors.toList());
        return fileList;
    }

    public List<BoardAttach> createAttach(final Long boardId, List<Long> files) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 데이터가 없습니다."));
        List<BoardAttach> attachList = files.stream().map(fileInfoId -> this.createAttach(board, fileInfoId)).collect(Collectors.toList());
        return attachList;
    }

    public BoardAttach createAttach(final Board board, Long fileInfoId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileInfoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 파일이 없습니다."));

        BoardAttach boardAttach = new BoardAttach(board,fileInfo);
        return boardAttachRepository.save(boardAttach);
    }
}
