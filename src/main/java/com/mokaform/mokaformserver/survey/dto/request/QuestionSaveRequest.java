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
    private Survey survey;

    @NotNull
    private String title;

    @NotNull
    private Long index;

    @NotNull
    private QuestionType type;

    @NotNull
    private Boolean is_multi_answer;

    @Builder
    public QuestionSaveRequest(Survey survey, String title, Long index, QuestionType type, Boolean is_multi_answer) {
        this.survey = survey;
        this.title = title;
        this.index = index;
        this.type = type;
        this.is_multi_answer = is_multi_answer;
    }
}