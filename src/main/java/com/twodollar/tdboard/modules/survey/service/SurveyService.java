package com.twodollar.tdboard.modules.survey.service;
import com.twodollar.tdboard.modules.survey.controller.request.SurveyRequest;
import com.twodollar.tdboard.modules.survey.entity.Survey;
import com.twodollar.tdboard.modules.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public long getTotalSurveySize(){
        return surveyRepository.count();
    }
    public List<Survey> getSurveys(Pageable pageable) {
        List<Survey> list = surveyRepository.findAll(pageable).getContent();
        if(list.size() == 0){
            new IllegalArgumentException("no such data");
        }
        return list;
    }

    public Survey getSurveyById(final Long id) {
        return surveyRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("no such data"));
    }

    public Survey createSurvey(final SurveyRequest createSurvey) {
        if(createSurvey == null) throw new IllegalArgumentException("item cannot be null");
        return surveyRepository.save(createSurvey.toEntity());
    }

    public Survey updateSurvey(final long id, final SurveyRequest updateSurvey) {
        Survey survey = getSurveyById(id);
        survey.setName(updateSurvey.getName());
        survey.setSurveyUrl(updateSurvey.getSurveyUrl());
        survey.setUseYn(updateSurvey.getUseYn());
        survey.setDelYn(updateSurvey.getDelYn());
        survey.setUpdatedAt(null);
        return surveyRepository.save(survey);
    }

    public void deleteSurveyById(final Long id) {
        Survey survey = getSurveyById(id);
        survey.setDelYn("Y");
        surveyRepository.save(survey);
    }

}
