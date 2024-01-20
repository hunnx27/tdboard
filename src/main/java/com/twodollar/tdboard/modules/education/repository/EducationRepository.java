package com.twodollar.tdboard.modules.education.repository;

import com.twodollar.tdboard.modules.education.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

}
