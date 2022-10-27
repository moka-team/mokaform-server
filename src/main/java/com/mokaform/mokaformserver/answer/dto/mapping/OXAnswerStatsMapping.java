package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OXAnswerStatsMapping {

    private Long questionIndex;

    private String title;

    private Boolean isYes;

    public OXAnswerStatsMapping(Long questionIndex,
                                String title,
                                Boolean isYes) {
        this.questionIndex = questionIndex;
        this.title = title;
        this.isYes = isYes;
    }
}
