package com.mokaform.mokaformserver.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class OXAnswerResponse {

    private final Long oxAnswerId;

    private final Long questionId;

    private final Boolean isYes;

    public OXAnswerResponse(OXAnswer oxAnswer) {
        this.oxAnswerId = oxAnswer.getOxAnswerId();
        this.questionId = oxAnswer.getAnswer().getQuestion().getQuestionId();
        this.isYes = oxAnswer.getIsYes();
    }
}
