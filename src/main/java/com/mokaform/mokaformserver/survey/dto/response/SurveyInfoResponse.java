package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import lombok.Getter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SurveyInfoResponse {

    private final Long surveyId;
    private final String title;
    private final String summary;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Boolean isAnonymous;
    private final Boolean isPublic;
    private final String sharingKey;

    private final Long surveyeeCount;

    public SurveyInfoResponse(SurveyInfoMapping surveyInfoMapping) {
        this.surveyId = surveyInfoMapping.getSurveyId();
        this.title = surveyInfoMapping.getTitle();
        this.summary = surveyInfoMapping.getSummary();
        this.startDate = surveyInfoMapping.getStartDate();
        this.endDate = surveyInfoMapping.getEndDate();
        this.isAnonymous = surveyInfoMapping.getIsAnonymous();
        this.isPublic = surveyInfoMapping.getIsPublic();
        this.sharingKey = surveyInfoMapping.getSharingKey();
        this.surveyeeCount = surveyInfoMapping.getSurveyeeCount();
    }
}
