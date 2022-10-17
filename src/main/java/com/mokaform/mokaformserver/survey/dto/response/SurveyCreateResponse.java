package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SurveyCreateResponse {

    private final Long surveyId;
    private final String sharingKey;

    public SurveyCreateResponse(Long surveyId, String sharingKey) {
        this.surveyId = surveyId;
        this.sharingKey = sharingKey;
    }
}
