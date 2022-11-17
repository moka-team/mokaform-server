package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.exception.errorcode.ErrorCode;
import com.mokaform.mokaformserver.common.exception.errorcode.SurveyErrorCode;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.common.util.UserUtilService;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.domain.SurveyCategory;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.survey.dto.mapping.SubmittedSurveyInfoMapping;
import com.mokaform.mokaformserver.survey.dto.mapping.SurveyInfoMapping;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.request.SurveyUpdateRequest;
import com.mokaform.mokaformserver.survey.dto.response.*;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyCategoryRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;
    private final SurveyCategoryRepository surveyCategoryRepository;

    private final UserUtilService userUtilService;

    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository,
                         SurveyCategoryRepository surveyCategoryRepository,
                         UserUtilService userUtilService) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
        this.surveyCategoryRepository = surveyCategoryRepository;
        this.userUtilService = userUtilService;
    }

    @Transactional
    public SurveyCreateResponse createSurvey(SurveyCreateRequest request, String userEmail) {
        User user = userUtilService.getUser(userEmail);

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

        return new SurveyCreateResponse(savedSurvey.getSurveyId(), savedSurvey.getSharingKey());
    }

    @Transactional(readOnly = true)
    public SurveyDetailsResponse getSurveyDetailsById(Long surveyId, String userEmail) {
        userUtilService.checkUser(userEmail);
        Survey survey = getSurveyById(surveyId);

        return getSurveyDetails(survey);
    }

    @Transactional(readOnly = true)
    public SurveyDetailsResponse getSurveyDetailsBySharingKey(String sharingKey, String userEmail) {
        userUtilService.checkUser(userEmail);
        Survey survey = getSurveyBySharingKey(sharingKey);

        return getSurveyDetails(survey);
    }

    @Transactional(readOnly = true)
    public PageResponse<SurveyInfoResponse> getSurveyInfos(Pageable pageable, String userEmail) {
        User user = userEmail == null ? null : userUtilService.getUser(userEmail);
        Page<SurveyInfoMapping> surveyInfos = surveyRepository.findSurveyInfos(pageable, user == null ? null : user.getId());
        return new PageResponse<>(
                surveyInfos.map(surveyInfo ->
                        new SurveyInfoResponse(surveyInfo, getSurveyCategories(surveyInfo.getSurveyId()))));
    }

    @Transactional(readOnly = true)
    public PageResponse<SurveyInfoResponse> getRecommendedSurveyInfos(Pageable pageable, String categoryName) {
        Page<SurveyInfoMapping> surveyInfos = surveyRepository.findRecommendedSurveyInfos(pageable, Category.getCategory(categoryName));
        return new PageResponse<>(
                surveyInfos.map(surveyInfo ->
                        new SurveyInfoResponse(surveyInfo, getSurveyCategories(surveyInfo.getSurveyId()))));
    }

    @Transactional(readOnly = true)
    public PageResponse<SubmittedSurveyInfoResponse> getSubmittedSurveyInfos(Pageable pageable, String userEmail) {
        User user = userUtilService.getUser(userEmail);
        Page<SubmittedSurveyInfoMapping> surveyInfos = surveyRepository.findSubmittedSurveyInfos(pageable, user.getId());

        if ("surveyeeCount".equals(pageable.getSort().get().findFirst().get().getProperty())) {
            Comparator<SubmittedSurveyInfoResponse> comparator = null;

            if ("ASC".equals(pageable.getSort().get().findFirst().get().getDirection().name())) {
                comparator = Comparator.comparing(SubmittedSurveyInfoResponse::getSurveyeeCount);
            } else {
                comparator = Comparator.comparing(SubmittedSurveyInfoResponse::getSurveyeeCount).reversed();
            }

            List<SubmittedSurveyInfoResponse> content = surveyInfos.stream()
                    .map(submittedSurveyInfo ->
                            SubmittedSurveyInfoResponse.builder()
                                    .surveyInfoMapping(submittedSurveyInfo)
                                    .surveyeeCount(surveyRepository.countSurveyee(submittedSurveyInfo.getSurveyId()))
                                    .surveyCategory(getSurveyCategories(submittedSurveyInfo.getSurveyId()))
                                    .build())
                    .sorted(comparator)
                    .collect(Collectors.toList());
            Page<SubmittedSurveyInfoResponse> responsePage = new PageImpl<>(content, surveyInfos.getPageable(), surveyInfos.getTotalElements());
            return new PageResponse<>(responsePage);
        }

        return new PageResponse<>(
                surveyInfos.map(submittedSurveyInfo ->
                        SubmittedSurveyInfoResponse.builder()
                                .surveyInfoMapping(submittedSurveyInfo)
                                .surveyeeCount(surveyRepository.countSurveyee(submittedSurveyInfo.getSurveyId()))
                                .surveyCategory(getSurveyCategories(submittedSurveyInfo.getSurveyId()))
                                .build()));
    }

    @Transactional
    public SurveyDeleteResponse deleteSurvey(Long surveyId, String userEmail) {
        User user = userUtilService.getUser(userEmail);
        Survey survey = getSurveyById(surveyId);
        validateUserAuthority(user, survey, SurveyErrorCode.NO_PERMISSION_TO_DELETE_SURVEY);
        survey.updateIsDeleted(true);
        return new SurveyDeleteResponse(survey.getSurveyId());
    }

    @Transactional
    public void updateSurveyInfoAndQuestions(Long surveyId, String userEmail, SurveyUpdateRequest request) {
        User user = userUtilService.getUser(userEmail);
        Survey survey = getSurveyById(surveyId);
        validateUserAuthority(user, survey, SurveyErrorCode.NO_PERMISSION_TO_UPDATE_SURVEY);

        /*
         * 설문 정보 업데이트
         */
        survey.updateTitle(request.getTitle());
        survey.updateSummary(request.getSummary());
        survey.updateStartDate(request.getStartDate());
        survey.updateEndDate(request.getEndDate());
        survey.updateAnonymous(request.getIsAnonymous());
        survey.updatePublic(request.getIsPublic());

        /*
         * 설문 카테고리 업데이트
         */
        List<Category> categoriesToUpdate = request.getCategories();
        List<SurveyCategory> existingSurveyCategories = getSurveyCategories(surveyId);
        existingSurveyCategories.forEach(category -> {
            Optional<Category> categoryToUpdate = categoriesToUpdate
                    .stream()
                    .filter(c -> c.equals(category.getCategory()))
                    .findFirst();
            categoryToUpdate.ifPresentOrElse(
                    c -> categoriesToUpdate.remove(c),
                    () -> {
                        category.unsetSurvey();
                        surveyCategoryRepository.delete(category);
                    }
            );
        });
        categoriesToUpdate.forEach(category ->
                saveSurveyCategory(new SurveyCategory(category, survey)));

        /*
         * 질문 업데이트
         */
        List<Question> existingQuestions = getQuestions(survey.getSurveyId());
        List<MultipleChoiceQuestion> existingMultiQuestions = existingQuestions.stream()
                .filter(Question::getIsMultiAnswer)
                .map(question ->
                        getMultipleChoiceQuestions(question.getQuestionId()))
                .collect(ArrayList::new, List::addAll, List::addAll);

        List<SurveyUpdateRequest.MultiQuestion> multiQuestionsToUpdate = request.getMultiQuestions();
        existingMultiQuestions.forEach(multiQuestion -> {
            Optional<SurveyUpdateRequest.MultiQuestion> multiQuestionToUpdate = multiQuestionsToUpdate
                    .stream()
                    .filter(m -> m.getMultiQuestionId() == multiQuestion.getMultiQuestionId())
                    .findFirst();
            multiQuestionToUpdate.ifPresentOrElse(
                    m -> multiQuestion.updateMultipleChoiceQuestion(m.getType(), m.getContent(), m.getIndex()),
                    () -> {
                        multiQuestion.unsetQuestion();
                        multiChoiceQuestionRepository.delete(multiQuestion);
                    }
            );
        });

        List<SurveyUpdateRequest.Question> questionsToUpdate = request.getQuestions();
        existingQuestions.forEach(question -> {
            Optional<SurveyUpdateRequest.Question> questionToUpdate = questionsToUpdate
                    .stream()
                    .filter(q -> q.getQuestionId() == question.getQuestionId())
                    .findFirst();
            questionToUpdate.ifPresentOrElse(
                    q -> question.updateQuestion(q.getIndex(), q.getTitle(), q.getType(), q.getIsMultipleAnswer()),
                    () -> questionRepository.delete(question));
        });

        questionsToUpdate
                .stream()
                .filter(question ->
                        question.getQuestionId() == null)
                .forEach(question -> {
                    Question savedQuestion = saveQuestion(Question.builder()
                            .survey(survey)
                            .title(question.getTitle())
                            .index(question.getIndex())
                            .type(question.getType())
                            .isMultiAnswer(question.getIsMultipleAnswer())
                            .build());

                    if (question.getIsMultipleAnswer()) {
                        multiQuestionsToUpdate
                                .stream()
                                .filter(multiQuestion ->
                                        multiQuestion.getQuestionIndex() == question.getIndex()
                                                && multiQuestion.getMultiQuestionId() == null)
                                .forEach(multiQuestion ->
                                        saveMultiChoiceQuestion(
                                                MultipleChoiceQuestion.builder()
                                                        .question(savedQuestion)
                                                        .multiQuestionType(multiQuestion.getType())
                                                        .multiQuestionContent(multiQuestion.getContent())
                                                        .multiQuestionIndex(multiQuestion.getIndex())
                                                        .build())
                                );
                    }
                });
    }

    private SurveyDetailsResponse getSurveyDetails(Survey survey) {
        List<SurveyCategory> surveyCategories = getSurveyCategories(survey.getSurveyId());
        List<Question> questions = getQuestions(survey.getSurveyId());
        ArrayList<MultipleChoiceQuestion> multiQuestions = questions.stream()
                .filter(Question::getIsMultiAnswer)
                .map(question ->
                        getMultipleChoiceQuestions(question.getQuestionId()))
                .collect(ArrayList::new, List::addAll, List::addAll);

        return SurveyDetailsResponse.builder()
                .survey(survey)
                .surveyCategories(surveyCategories)
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

    private List<SurveyCategory> getSurveyCategories(Long surveyId) {
        Survey survey = getSurveyById(surveyId);

        return surveyCategoryRepository.findSurveyCategoriesBySurvey(survey);
    }

    private void validateUserAuthority(User user, Survey survey, ErrorCode errorCode) {
        if (user.getId() != survey.getUser().getId()) {
            throw new ApiException(errorCode);
        }
    }
}
