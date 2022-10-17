package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OXAnswerStatsMapping {

    private Long questionId;

    private Boolean isYes;

    public OXAnswerStatsMapping(Long questionId, Boolean isYes) {
        this.questionId = questionId;
        this.isYes = isYes;
    }
}
