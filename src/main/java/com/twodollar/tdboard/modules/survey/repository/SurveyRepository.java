package com.twodollar.tdboard.modules.survey.repository;

import com.twodollar.tdboard.modules.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
