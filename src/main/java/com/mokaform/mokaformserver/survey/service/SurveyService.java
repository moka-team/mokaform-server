package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDetailsResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyCategoryRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;
    private final SurveyCategoryRepository surveyCategoryRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository,
                         SurveyCategoryRepository surveyCategoryRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
        this.surveyCategoryRepository = surveyCategoryRepository;
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

        request.getCategories().forEach(category ->
                saveSurveyCategory(new SurveyCategory(category, savedSurvey)));

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

    @Transactional(readOnly = true)
    public SurveyDetailsResponse getSurveyDetailsById(Long surveyId) {
        Survey survey = getSurveyById(surveyId);

        return getSurveyDetails(survey);
    }

    @Transactional(readOnly = true)
    public SurveyDetailsResponse getSurveyDetailsBySharingKey(String sharingKey) {
        Survey survey = getSurveyBySharingKey(sharingKey);

        return getSurveyDetails(survey);
    }

    public PageResponse<SurveyInfoResponse> getSurveyInfos(Pageable pageable) {
        Page<SurveyInfoMapping> surveyInfos = surveyRepository.findSurveyInfos(pageable);
        return new PageResponse<>(surveyInfos.map(SurveyInfoResponse::new));
    }

    private SurveyDetailsResponse getSurveyDetails(Survey survey) {
        List<Question> questions = getQuestions(survey.getSurveyId());
        ArrayList<MultipleChoiceQuestion> multiQuestions = questions.stream()
                .filter(Question::getIsMultiAnswer)
                .map(question ->
                        getMultipleChoiceQuestions(question.getQuestionId()))
                .collect(ArrayList::new, List::addAll, List::addAll);

        return SurveyDetailsResponse.builder()
                .survey(survey)
                .questions(questions)
                .multipleChoiceQuestions(multiQuestions)
                .build();
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

    private void saveSurveyCategory(SurveyCategory surveyCategory) {
        surveyCategoryRepository.save(surveyCategory);
    }

    private Survey getSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
    }

    private Survey getSurveyBySharingKey(String sharingKey) {
        return surveyRepository.findBySharingKey(sharingKey)
                .orElseThrow(() -> new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
    }

    private Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
    }

    private List<Question> getQuestions(Long surveyId) {
        Survey survey = getSurveyById(surveyId);

        return questionRepository.findQuestionsBySurvey(survey);
    }

    private List<MultipleChoiceQuestion> getMultipleChoiceQuestions(Long questionId) {
        Question question = getQuestionById(questionId);

        return multiChoiceQuestionRepository.findMultipleChoiceQuestionsByQuestion(question);
    }

}
