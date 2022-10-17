package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AnswerInfoMapping {

    private Long answerId;
    private Long questionId;

    public AnswerInfoMapping(Long answerId, Long questionId) {
        this.answerId = answerId;
        this.questionId = questionId;
    }
}
