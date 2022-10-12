package com.mokaform.mokaformserver.survey.dto.request;

import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class MCQuestionSaveRequest {
    @NotNull
    private MultiQuestionType multiQuestionType;

    @NotNull
    private String multiQuestionContent;

    @NotNull
    private Integer multiQuestionIndex;

    @NotNull
    private Long questionId;

    @Builder
    public MCQuestionSaveRequest(MultiQuestionType multiQuestionType,
                                 String multiQuestionContent, Integer multiQuestionIndex, Long questionId) {
        this.multiQuestionType = multiQuestionType;
        this.multiQuestionContent = multiQuestionContent;
        this.multiQuestionIndex = multiQuestionIndex;
        this.questionId = questionId;
    }

}
