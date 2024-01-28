package com.twodollar.tdboard.modules.fileInfo.service;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.repository.BoardRepository;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.facility.controller.request.FacilityRequest;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.fileInfo.entity.BoardAttach;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.entity.enums.AttachType;
import com.twodollar.tdboard.modules.fileInfo.repository.BoardAttachRepository;
import com.twodollar.tdboard.modules.fileInfo.repository.FileInfoRepository;
import com.twodollar.tdboard.modules.fileInfo.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachService {
    private final BoardAttachRepository boardAttachRepository;
    private final FileInfoService fileInfoService;
    private final BoardService boardService;
    private final FileUtils fileUtils;

    public List<FileInfo> getAttaches(Long boardId) {
        Board board = boardService.getBoardById(boardId);
        List<BoardAttach> list = boardAttachRepository.getBoardAttachesByBoard(board).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 첨부파일이 없습니다.")));

        List<FileInfo> fileList = list.stream().map(boardAttach -> boardAttach.getFileInfo()).collect(Collectors.toList());
        return fileList;
    }

    public List<BoardAttach> createAttach(final Long boardId, List<Long> files) {
        Board board = boardService.getBoardById(boardId);
        List<BoardAttach> attachList = files.stream().map(fileInfoId -> this.createAttach(board, fileInfoId)).collect(Collectors.toList());
        return attachList;
    }

    public BoardAttach createAttach(final Board board, Long fileInfoId) {
        FileInfo fileInfo = fileInfoService.getFileInfoById(fileInfoId);
        BoardAttach boardAttach = new BoardAttach(board,fileInfo);
        return boardAttachRepository.save(boardAttach);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<BoardAttach> createUpdate(final Long boardId, List<Long> files) {
        Board board = boardService.getBoardById(boardId);

        // 0. 기존 파일첨부리스트 조회
        List<BoardAttach> tempList = boardAttachRepository.getBoardAttachesByBoard(board).orElse(null);

        // 1. 삭제
        boardAttachRepository.deleteBoardAttachesByBoard(board);

        // 2. 등록
        List<BoardAttach> attachList = this.createAttach(boardId, files);

        // 3. 파일삭제 처리
        List<Long> newFileList = attachList.stream().map(boardAttach -> boardAttach.getFileInfo().getId()).collect(Collectors.toList());
        List<FileInfo> dellist = tempList.stream()
                .filter(temp -> {
                    boolean isNotExist = !newFileList.contains(temp.getFileInfo().getId());
                    return isNotExist;
                })
                .map(temp -> temp.getFileInfo())
                .collect(Collectors.toList());

        dellist.stream().forEach(fileInfo -> {
            boolean isDeleted = fileUtils.deleteFile(fileInfo.getStoredPath());
            fileInfoService.deleteFileInfoById(fileInfo.getId());
            log.info("isDeleted : {}", isDeleted);
        });


        return attachList;
    }
}
