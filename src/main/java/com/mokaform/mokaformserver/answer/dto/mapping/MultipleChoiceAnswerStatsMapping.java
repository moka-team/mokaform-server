package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MultipleChoiceAnswerStatsMapping {

    private String title;

    private String multiQuestionContent;

    public MultipleChoiceAnswerStatsMapping(String title, String multiQuestionContent) {
        this.title = title;
        this.multiQuestionContent = multiQuestionContent;
    }
}
