package com.mokaform.mokaformserver.survey.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.survey.domain.enums.MultiQuestionType;
import com.mokaform.mokaformserver.survey.domain.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class SurveyUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String summary;

    @NotNull
    private Boolean isAnonymous;

    @NotNull
    private Boolean isPublic;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private List<Category> categories;

    private List<Question> questions;

    private List<MultiQuestion> multiQuestions;

    @Builder
    public SurveyUpdateRequest(String title, String summary,
                               LocalDate startDate, LocalDate endDate,
                               Boolean isAnonymous, Boolean isPublic,
                               List<Category> categories, List<Question> questions,
                               List<MultiQuestion> multiQuestions) {
        this.title = title;
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.categories = categories;
        this.questions = questions;
        this.multiQuestions = multiQuestions;
    }

    @NoArgsConstructor
    @Getter
    public static class Question {

        private Long questionId;

        @NotNull
        private Long index;

        @NotBlank
        private String title;

        @NotBlank
        private QuestionType type;

        @NotNull
        private Boolean isMultipleAnswer;

        @Builder
        public Question(Long questionId, Long index,
                        String title, QuestionType type,
                        Boolean isMultipleAnswer) {
            this.questionId = questionId;
            this.index = index;
            this.title = title;
            this.type = type;
            this.isMultipleAnswer = isMultipleAnswer;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class MultiQuestion {

        private Long multiQuestionId;

        private Long questionId;

        @NotNull
        private Long multiQuestionIndex;

        @NotNull
        private Long questionIndex;

        @NotBlank
        private String multiQuestionContent;

        @NotNull
        private MultiQuestionType multiQuestionType;

        @Builder
        public MultiQuestion(Long multiQuestionId, Long questionId,
                             Long questionIndex, Long index,
                             String content, MultiQuestionType type) {
            this.multiQuestionId = multiQuestionId;
            this.questionId = questionId;
            this.multiQuestionIndex = questionIndex;
            this.questionIndex = index;
            this.multiQuestionContent = content;
            this.multiQuestionType = type;
        }
    }
}
