package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EssayAnswerStatsMapping {

    private String title;

    private String answerContent;

    public EssayAnswerStatsMapping(String title, String answerContent) {
        this.title = title;
        this.answerContent = answerContent;
    }
}
