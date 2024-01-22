package com.twodollar.tdboard.modules.survey.controller.request;

import com.twodollar.tdboard.modules.survey.entity.Survey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SurveyRequest {
    private Long id;
    private String name;
    private String surveyUrl;
    // 사용여부
    private String useYn;
    // 삭제여부
    private String delYn;

    public Survey toEntity(){
        return Survey.builder().
                id(this.id).
                name(this.name).
                surveyUrl(this.surveyUrl).
                useYn(this.useYn).
                delYn(this.delYn).
                build();
    }
}
