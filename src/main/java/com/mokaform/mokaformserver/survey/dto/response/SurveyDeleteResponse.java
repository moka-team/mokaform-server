package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SurveyDeleteResponse {

    private final Long surveyId;

    public SurveyDeleteResponse(Long surveyId) {
        this.surveyId = surveyId;
    }
}
