package com.twodollar.tdboard.modules.file.repository;

import com.twodollar.tdboard.modules.facility.entity.Facility;
import com.twodollar.tdboard.modules.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
