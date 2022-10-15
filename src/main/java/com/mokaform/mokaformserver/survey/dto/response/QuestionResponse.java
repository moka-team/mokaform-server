package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class QuestionResponse {

    private final Long questionId;
    private final Long index;
    private final String title;
    private final QuestionType type;
    private final Boolean isMultipleAnswer;

    public QuestionResponse(Question question) {
        this.questionId = question.getQuestionId();
        this.index = question.getIndex();
        this.title = question.getTitle();
        this.type = question.getType();
        this.isMultipleAnswer = question.getIsMultiAnswer();
    }
}
