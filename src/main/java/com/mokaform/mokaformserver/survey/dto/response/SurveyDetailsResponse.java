package com.mokaform.mokaformserver.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class SurveyDetailsResponse extends SurveyResponse {

    private final List<Category> surveyCategories;
    private final List<QuestionResponse> questions;
    private final List<MultipleChoiceQuestionResponse> multiQuestions;

    @Builder
    public SurveyDetailsResponse(Survey survey,
                                 List<SurveyCategory> surveyCategories,
                                 List<Question> questions,
                                 List<MultipleChoiceQuestion> multipleChoiceQuestions) {
        super(survey);
        this.surveyCategories = surveyCategories
                .stream()
                .map(SurveyCategory::getCategory)
                .collect(Collectors.toList());
        this.questions = questions.stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
        this.multiQuestions = multipleChoiceQuestions.stream()
                .map(MultipleChoiceQuestionResponse::new)
                .collect(Collectors.toList());
    }
}
