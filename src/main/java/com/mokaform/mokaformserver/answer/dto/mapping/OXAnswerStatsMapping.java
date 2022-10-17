package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OXAnswerStatsMapping {

    private String title;

    private Boolean isYes;

    public OXAnswerStatsMapping(String title, Boolean isYes) {
        this.title = title;
        this.isYes = isYes;
    }
}
