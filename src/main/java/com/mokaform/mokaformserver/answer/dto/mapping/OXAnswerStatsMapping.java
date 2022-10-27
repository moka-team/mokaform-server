package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OXAnswerStatsMapping {

    private Long questionId;

    private Long questionIndex;

    private String title;

    private Boolean isYes;

    @Builder
    public OXAnswerStatsMapping(Long questionId, Long questionIndex,
                                String title, Boolean isYes) {
        this.questionId = questionId;
        this.questionIndex = questionIndex;
        this.title = title;
        this.isYes = isYes;
    }
}
