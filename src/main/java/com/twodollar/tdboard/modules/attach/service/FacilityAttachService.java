package com.twodollar.tdboard.modules.attach.service;

import com.twodollar.tdboard.modules.attach.entity.BoardAttach;
import com.twodollar.tdboard.modules.attach.entity.FacilityAttach;
import com.twodollar.tdboard.modules.attach.repository.BoardAttachRepository;
import com.twodollar.tdboard.modules.attach.repository.FacilityAttachRepository;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.facility.service.FacilityService;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.service.FileInfoService;
import com.twodollar.tdboard.modules.fileInfo.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacilityAttachService {
    private final FacilityAttachRepository facilityAttachRepository;
    private final FileInfoService fileInfoService;
    private final FacilityService facilityService;
    private final FileUtils fileUtils;

    public List<FileInfo> getAttaches(Long facilityId) {
        Facility facility = facilityService.getFacilityById(facilityId);
        List<FacilityAttach> list = facilityAttachRepository.getFacilityAttachesByFacility(facility).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 첨부파일이 없습니다.")));

        List<FileInfo> fileList = list.stream().map(facilityAttach -> facilityAttach.getFileInfo()).collect(Collectors.toList());
        return fileList;
    }

    public List<FacilityAttach> createAttach(final Long facilityId, List<Long> files) {
        Facility facility = facilityService.getFacilityById(facilityId);
        List<FacilityAttach> attachList = new ArrayList<>();
        if(files!=null) {
            attachList = files.stream().map(fileInfoId -> this.createAttach(facility, fileInfoId)).collect(Collectors.toList());
        }
        return attachList;
    }

    public FacilityAttach createAttach(final Facility facility, Long fileInfoId) {
        FileInfo fileInfo = fileInfoService.getFileInfoById(fileInfoId);
        FacilityAttach facilityAttach = new FacilityAttach(facility,fileInfo);
        return facilityAttachRepository.save(facilityAttach);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<FacilityAttach> createUpdate(final Long facilityId, List<Long> files) {
        Facility facility = facilityService.getFacilityById(facilityId);

        // 0. 기존 파일첨부리스트 조회
        List<FacilityAttach> tempList = facilityAttachRepository.getFacilityAttachesByFacility(facility).orElse(null);

        // 1. 삭제
        facilityAttachRepository.deleteFacilityAttachesByFacility(facility);

        // 2. 등록
        List<FacilityAttach> attachList = this.createAttach(facilityId, files);

        // 3. 파일삭제 처리
        List<Long> newFileList = attachList.stream().map(facilityAttach -> facilityAttach.getFileInfo().getId()).collect(Collectors.toList());
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
