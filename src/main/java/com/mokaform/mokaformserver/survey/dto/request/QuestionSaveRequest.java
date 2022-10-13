package com.mokaform.mokaformserver.survey.dto.request;

import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@NoArgsConstructor
@Getter
public class QuestionSaveRequest {
    @NotNull
    private String title;

    @NotNull
    private Long index;

    @NotNull
    private QuestionType type;

    @NotNull
    private Boolean isMultiAnswer;

    @NotNull
    private Long surveyId;

    @Builder
    public QuestionSaveRequest(String title, Long index, QuestionType type, Boolean isMultiAnswer, Long surveyId) {
        this.title = title;
        this.index = index;
        this.type = type;
        this.isMultiAnswer = isMultiAnswer;
        this.surveyId = surveyId;
    }
}
