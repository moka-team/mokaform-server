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

    private Long yesCount;

    private Long noCount;

    @Builder
    public OXAnswerStatsMapping(Long questionId, Long questionIndex,
                                String title, Long yesCount,
                                Long noCount) {
        this.questionId = questionId;
        this.questionIndex = questionIndex;
        this.title = title;
        this.yesCount = yesCount;
        this.noCount = noCount;
    }
}
