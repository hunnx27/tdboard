package com.twodollar.tdboard.modules.education.repository;

import com.twodollar.tdboard.modules.education.entity.Education;
import com.twodollar.tdboard.modules.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    int countEducationByDelYn(String delYn);
    Optional<List<Education>> getEducationsByDelYnOrderByCreatedAtDesc(String DelYn, Pageable pageable);
    Optional<List<Education>> getEducationsByStartDateContainingOrEndDateContainingAndDelYn(String yearMonth1, String yearMonth2, String delYn);
}
