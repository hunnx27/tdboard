package com.twodollar.tdboard.modules.attach.repository;

import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.attach.entity.EducationAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationAttachRepository extends JpaRepository<EducationAttach, Long> {
    Optional<List<EducationAttach>> getEducationAttachesByEducation(Education education);

    void deleteEducationAttachesByEducation(Education education);


}
