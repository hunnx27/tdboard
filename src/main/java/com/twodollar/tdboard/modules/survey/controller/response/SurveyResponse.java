package com.twodollar.tdboard.modules.survey.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
public class SurveyResponse {
    private Long id;
    private String name;
    private String surveyUrl;
    // 생성일
    @CreationTimestamp
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public SurveyResponse(Long id, String name, String surveyUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.surveyUrl = surveyUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
