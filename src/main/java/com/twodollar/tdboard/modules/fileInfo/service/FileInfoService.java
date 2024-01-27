package com.twodollar.tdboard.modules.fileInfo.service;
import com.twodollar.tdboard.modules.fileInfo.controller.request.FileInfoRequest;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.repository.FileInfoRepository;
import com.twodollar.tdboard.modules.fileInfo.util.FileUtils;
import com.twodollar.tdboard.modules.fileInfo.util.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileInfoService {
    private final FileInfoRepository fileInfoRepository;
    private final FileUtils fileUtils;
    @Value("${file.upload.path}")
    private String UPLOAD_DIR;

    public long getTotalFileInfoSize(){
        return fileInfoRepository.count();
    }
    public List<FileInfo> getFileInfos(Pageable pageable) {
        List<FileInfo> list = fileInfoRepository.findAll(pageable).getContent();
        if(list.size() == 0){
            new IllegalArgumentException("no such data");
        }
        return list;
    }

    public FileInfo getFileInfoById(final Long id) {
        return fileInfoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public List<FileInfo> createFileInfo(final FileInfoRequest createFileInfo, String userId) {
        if(createFileInfo == null) throw new IllegalArgumentException("item cannot be null");
        String dir = createFileInfo.getUploadType().name();
        List<FileInfoDto> fileInfoDtos = fileUtils.uploadFiles(createFileInfo.getFiles(), dir);
        List<FileInfo> files = fileInfoDtos.stream().map(fileInfoDto -> {
            FileInfo fileInfo = new FileInfo(fileInfoDto);
            fileInfo.setUserId(userId);
            return fileInfo;
        }).collect(Collectors.toList());
        List<FileInfo> savedfiles = fileInfoRepository.saveAll(files);
        return savedfiles;
    }

    public void deleteFileInfoById(final Long id) {
        fileInfoRepository.deleteById(id);
    }

}
