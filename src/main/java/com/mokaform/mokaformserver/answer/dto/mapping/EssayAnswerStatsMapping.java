package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EssayAnswerStatsMapping {

    private Long questionId;

    private String answerContent;

    public EssayAnswerStatsMapping(Long questionId, String answerContent) {
        this.questionId = questionId;
        this.answerContent = answerContent;
    }
}
