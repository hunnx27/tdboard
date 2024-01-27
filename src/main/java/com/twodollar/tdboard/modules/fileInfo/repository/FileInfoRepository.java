package com.twodollar.tdboard.modules.fileInfo.repository;

import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

}
