package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EssayAnswerStatsMapping {

    private Long questionIndex;

    private String title;

    private String answerContent;

    public EssayAnswerStatsMapping(Long questionIndex,
                                   String title,
                                   String answerContent) {
        this.questionIndex = questionIndex;
        this.title = title;
        this.answerContent = answerContent;
    }
}
