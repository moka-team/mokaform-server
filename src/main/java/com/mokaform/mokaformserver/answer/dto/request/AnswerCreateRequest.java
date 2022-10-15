package com.mokaform.mokaformserver.answer.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
public class AnswerCreateRequest {
    private List<EssayAnswer> essayAnswers;

    private List<MultipleChoiceAnswer> multipleChoiceAnswers;

    private List<OXAnswer> oxAnswers;

    @Builder
    public  AnswerCreateRequest(List<EssayAnswer> essayAnswers, List<MultipleChoiceAnswer> multipleChoiceAnswers, List<OXAnswer> oxAnswers) {
        this.essayAnswers = essayAnswers;
        this.multipleChoiceAnswers = multipleChoiceAnswers;
        this.oxAnswers = oxAnswers;
    }

    @NoArgsConstructor
    @Getter
    public static class EssayAnswer {
        @NotNull
        private Long questionId;
        @NotBlank
        private String answerContent;

        @Builder
        public EssayAnswer(Long questionId, String answerContent) {
            this.questionId = questionId;
            this.answerContent = answerContent;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class MultipleChoiceAnswer {
        @NotNull
        private Long questionId;

        @NotNull
        private Long multiQuestionId;

        @Builder
        public MultipleChoiceAnswer(Long questionId, Long multiQuestionId) {
            this.multiQuestionId = multiQuestionId;
            this.questionId = questionId;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class OXAnswer {
        @NotNull
        private Long questionId;
        @NotNull
        private Boolean isYes;

        @Builder
        public OXAnswer(Long questionId, Boolean isYes) {
            this.questionId = questionId;
            this.isYes = isYes;
        }
    }

}
