package com.twodollar.tdboard.modules.file.service;
import com.twodollar.tdboard.modules.file.controller.request.FileRequest;
import com.twodollar.tdboard.modules.file.entity.File;
import com.twodollar.tdboard.modules.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public long getTotalFileSize(){
        return fileRepository.count();
    }
    public List<File> getFiles(Pageable pageable) {
        List<File> list = fileRepository.findAll(pageable).getContent();
        if(list.size() == 0){
            new IllegalArgumentException("no such data");
        }
        return list;
    }

    public File getFileById(final Long id) {
        return fileRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public File createFile(final FileRequest createFile) {
        if(createFile == null) throw new IllegalArgumentException("item cannot be null");
        return fileRepository.save(createFile.toEntity());
    }

    public void deleteFileById(final Long id) {
        fileRepository.deleteById(id);
    }

}
