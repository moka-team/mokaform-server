package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EssayAnswerStatsMapping {

    private Long questionId;

    private Long questionIndex;

    private String title;

    private String answerContent;

    @Builder
    public EssayAnswerStatsMapping(Long questionId, Long questionIndex,
                                   String title, String answerContent) {
        this.questionId = questionId;
        this.questionIndex = questionIndex;
        this.title = title;
        this.answerContent = answerContent;
    }
}
