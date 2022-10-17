package com.mokaform.mokaformserver.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class MultipleChoiceAnswerResponse {

    private final Long multipleChoiceAnswerId;

    private final Long questionId;

    private final Long multiQuestionId;

    public MultipleChoiceAnswerResponse(MultipleChoiceAnswer multipleChoiceAnswer) {
        this.multipleChoiceAnswerId = multipleChoiceAnswer.getMultipleChoiceAnswerId();
        this.questionId = multipleChoiceAnswer.getAnswer().getQuestion().getQuestionId();
        this.multiQuestionId = multipleChoiceAnswer.getMultipleChoiceQuestion().getMultiQuestionId();
    }
}
