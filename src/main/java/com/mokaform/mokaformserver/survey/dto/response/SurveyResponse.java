package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.survey.domain.Survey;
import lombok.Getter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SurveyResponse {

    private final Long surveyId;
    private final String title;
    private final String summary;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Boolean isAnonymous;
    private final Boolean isPublic;
    private final String sharing_key;

    private final int questionCount;

    public SurveyResponse(Survey survey) {
        this.surveyId = survey.getSurveyId();
        this.title = survey.getTitle();
        this.summary = survey.getSummary();
        this.startDate = survey.getStartDate();
        this.endDate = survey.getEndDate();
        this.isAnonymous = survey.getIsAnonymous();
        this.isPublic = survey.getIsPublic();
        this.sharing_key = survey.getSharingKey();
        this.questionCount = survey.getQuestions().size();
    }
}
