package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
    }

    @Transactional
    public SurveyCreateResponse createSurvey(SurveyCreateRequest request, User user) {
        Survey savedSurvey = saveSurvey(Survey.builder()
                .user(user)
                .title(request.getTitle())
                .summary(request.getSummary())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isAnonymous(request.getIsAnonymous())
                .isPublic(request.getIsPublic())
                .build());

        request.getQuestions()
                .forEach(question -> {
                    Question savedQuestion = saveQuestion(
                            Question.builder()
                                    .survey(savedSurvey)
                                    .title(question.getTitle())
                                    .index(question.getIndex())
                                    .type(question.getType())
                                    .isMultiAnswer(question.getIsMultipleAnswer())
                                    .build()
                    );
                    if (question.getIsMultipleAnswer()) {
                        request.getMultiQuestions()
                                .stream()
                                .filter(m ->
                                        m.getQuestionIndex() == question.getIndex())
                                .forEach(m ->
                                        saveMultiChoiceQuestion(
                                                MultipleChoiceQuestion.builder()
                                                        .question(savedQuestion)
                                                        .multiQuestionType(m.getType())
                                                        .multiQuestionContent(m.getContent())
                                                        .multiQuestionIndex(m.getIndex())
                                                        .build())
                                );
                    }
                });

        return new SurveyCreateResponse(savedSurvey.getSurveyId());
    }

    private Survey saveSurvey(Survey survey) {
        Survey savedSurvey = surveyRepository.save(survey);
        return savedSurvey;
    }

    private Question saveQuestion(Question question) {
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion;
    }

    private void saveMultiChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        multiChoiceQuestionRepository.save(multipleChoiceQuestion);
    }

}
