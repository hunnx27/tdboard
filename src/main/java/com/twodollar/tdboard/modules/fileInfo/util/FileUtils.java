package com.twodollar.tdboard.modules.fileInfo.util;

import com.twodollar.tdboard.modules.fileInfo.util.dto.FileInfoDto;
import com.twodollar.tdboard.modules.fileInfo.controller.request.FileInfoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {
    @Value("${file.upload.path}")
    private String UPLOAD_DIR;

    /**
     * 다중 파일 업로드
     * @param multipartFiles - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    public List<FileInfoDto> uploadFiles(final List<MultipartFile> multipartFiles, String dir) {
        List<FileInfoDto> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile, dir));
        }
        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public FileInfoDto uploadFile(final MultipartFile multipartFile, String dir) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String storedName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        String uploadPath = getUploadPath(dir, today) + File.separator + storedName;
        File uploadFile = new File(uploadPath);
        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storedPath = String.format("/upload/%s/%s/%s", today, dir, storedName);

        return FileInfoDto.builder()
                .originalName(multipartFile.getOriginalFilename())
                .storedName(storedName)
                .storedPath(storedPath)
                .size(multipartFile.getSize())
                .build();
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환
     * @return 업로드 경로
     */
    private String getUploadPath() {
        return makeDirectories(UPLOAD_DIR);
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(final String today, final String addPath) {
        return makeDirectories(UPLOAD_DIR + File.separator + today + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

}
