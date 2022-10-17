package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.survey.dto.mapping.SubmittedSurveyInfoMapping;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SubmittedSurveyInfoResponse {

    private final Long surveyId;
    private final String title;
    private final String summary;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Boolean isAnonymous;
    private final Boolean isPublic;
    private final String sharingKey;
    private final Boolean isDeleted;

    private final Long surveyeeCount;

    private final List<Category> surveyCategories;

    @Builder
    public SubmittedSurveyInfoResponse(SubmittedSurveyInfoMapping surveyInfoMapping,
                                       Long surveyeeCount,
                                       List<SurveyCategory> surveyCategory) {
        this.surveyId = surveyInfoMapping.getSurveyId();
        this.title = surveyInfoMapping.getTitle();
        this.summary = surveyInfoMapping.getSummary();
        this.startDate = surveyInfoMapping.getStartDate();
        this.endDate = surveyInfoMapping.getEndDate();
        this.isAnonymous = surveyInfoMapping.getIsAnonymous();
        this.isPublic = surveyInfoMapping.getIsPublic();
        this.sharingKey = surveyInfoMapping.getSharingKey();
        this.isDeleted = surveyInfoMapping.getIsDeleted();
        this.surveyeeCount = surveyeeCount;
        this.surveyCategories = surveyCategory
                .stream()
                .map(SurveyCategory::getCategory)
                .collect(Collectors.toList());
    }
}
