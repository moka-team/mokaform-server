package com.mokaform.mokaformserver.survey.dto.request;

import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class MCQuestionSaveRequest {
    @NotNull
    private Long question_id;

    @NotNull
    private MultiQuestionType multi_question_type;

    @NotNull
    private String multi_question_content;

    @NotNull
    private Integer multi_question_index;

    @Builder
    public MCQuestionSaveRequest(Survey survey, MultiQuestionType multi_question_type,
                                 String multi_question_content, Integer multi_question_index) {
        this.question_id = survey.getSurvey_id();
        this.multi_question_type = multi_question_type;
        this.multi_question_content = multi_question_content;
        this.multi_question_index = multi_question_index;
    }

}
