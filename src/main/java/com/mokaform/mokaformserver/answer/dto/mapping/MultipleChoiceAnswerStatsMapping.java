package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MultipleChoiceAnswerStatsMapping {

    private Long questionId;

    private Long multiQuestionId;

    public MultipleChoiceAnswerStatsMapping(Long questionId, Long multiQuestionId) {
        this.questionId = questionId;
        this.multiQuestionId = multiQuestionId;
    }
}
