package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MultipleChoiceAnswerStatsMapping {

    private Long questionIndex;

    private String title;

    private String multiQuestionContent;

    private Long multiQuestionContentCount;

    public MultipleChoiceAnswerStatsMapping(Long questionIndex,
                                            String title,
                                            String multiQuestionContent,
                                            Long multiQuestionContentCount) {
        this.questionIndex = questionIndex;
        this.title = title;
        this.multiQuestionContent = multiQuestionContent;
        this.multiQuestionContentCount = multiQuestionContentCount;
    }
}
