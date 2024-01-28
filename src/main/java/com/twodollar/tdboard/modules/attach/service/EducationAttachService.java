package com.twodollar.tdboard.modules.attach.service;

import com.twodollar.tdboard.modules.attach.entity.BoardAttach;
import com.twodollar.tdboard.modules.attach.entity.EducationAttach;
import com.twodollar.tdboard.modules.attach.repository.BoardAttachRepository;
import com.twodollar.tdboard.modules.attach.repository.EducationAttachRepository;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.education.service.EducationService;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.service.FileInfoService;
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
public class EducationAttachService {
    private final EducationAttachRepository educationAttachRepository;
    private final FileInfoService fileInfoService;
    private final EducationService educationService;
    private final FileUtils fileUtils;

    public List<FileInfo> getAttaches(Long educationId) {
        Education education = educationService.getEducationById(educationId);
        List<EducationAttach> list = educationAttachRepository.getEducationAttachesByEducation(education).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 첨부파일이 없습니다.")));

        List<FileInfo> fileList = list.stream().map(educationAttach -> educationAttach.getFileInfo()).collect(Collectors.toList());
        return fileList;
    }

    public List<EducationAttach> createAttach(final Long educationId, List<Long> files) {
        Education education = educationService.getEducationById(educationId);
        List<EducationAttach> attachList = files.stream().map(fileInfoId -> this.createAttach(education, fileInfoId)).collect(Collectors.toList());
        return attachList;
    }

    public EducationAttach createAttach(final Education education, Long fileInfoId) {
        FileInfo fileInfo = fileInfoService.getFileInfoById(fileInfoId);
        EducationAttach educationAttach = new EducationAttach(education,fileInfo);
        return educationAttachRepository.save(educationAttach);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<EducationAttach> createUpdate(final Long educationId, List<Long> files) {
        Education education = educationService.getEducationById(educationId);

        // 0. 기존 파일첨부리스트 조회
        List<EducationAttach> tempList = educationAttachRepository.getEducationAttachesByEducation(education).orElse(null);

        // 1. 삭제
        educationAttachRepository.deleteEducationAttachesByEducation(education);

        // 2. 등록
        List<EducationAttach> attachList = this.createAttach(educationId, files);

        // 3. 파일삭제 처리
        List<Long> newFileList = attachList.stream().map(educationAttach -> educationAttach.getFileInfo().getId()).collect(Collectors.toList());
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
