package com.mokaform.mokaformserver.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class EssayAnswerResponse {

    private Long essayAnswerId;

    private Long questionId;

    private String answerContent;

    public EssayAnswerResponse(EssayAnswer essayAnswer) {
        this.essayAnswerId = essayAnswer.getEssayAnswerId();
        this.questionId = essayAnswer.getAnswer().getQuestion().getQuestionId();
        this.answerContent = essayAnswer.getAnswerContent();
    }
}
