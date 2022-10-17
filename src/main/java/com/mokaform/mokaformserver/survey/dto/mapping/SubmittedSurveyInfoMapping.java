package com.mokaform.mokaformserver.survey.dto.mapping;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class SubmittedSurveyInfoMapping {

    private Long surveyId;
    private String title;
    private String summary;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isAnonymous;
    private Boolean isPublic;
    private String sharingKey;
    private Boolean isDeleted;

    @Builder
    public SubmittedSurveyInfoMapping(Long surveyId, String title,
                                      String summary, LocalDate startDate,
                                      LocalDate endDate, Boolean isAnonymous,
                                      Boolean isPublic, String sharingKey,
                                      Boolean isDeleted) {
        this.surveyId = surveyId;
        this.title = title;
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.sharingKey = sharingKey;
        this.isDeleted = isDeleted;
    }
}
