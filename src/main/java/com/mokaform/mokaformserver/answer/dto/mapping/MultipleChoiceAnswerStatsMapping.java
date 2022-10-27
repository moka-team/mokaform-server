package com.mokaform.mokaformserver.answer.dto.mapping;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MultipleChoiceAnswerStatsMapping {

    private Long questionId;

    private Long questionIndex;

    private String title;

    private String multiQuestionContent;

    private Long multiQuestionContentCount;

    @Builder
    public MultipleChoiceAnswerStatsMapping(Long questionId, Long questionIndex,
                                            String title, String multiQuestionContent,
                                            Long multiQuestionContentCount) {
        this.questionId = questionId;
        this.questionIndex = questionIndex;
        this.title = title;
        this.multiQuestionContent = multiQuestionContent;
        this.multiQuestionContentCount = multiQuestionContentCount;
    }
}
