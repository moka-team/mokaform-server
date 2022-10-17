package com.mokaform.mokaformserver.answer.service;

import com.mokaform.mokaformserver.answer.domain.Answer;
import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.EssayAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.MultipleChoiceAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.mapping.OXAnswerStatsMapping;
import com.mokaform.mokaformserver.answer.dto.request.AnswerCreateRequest;
import com.mokaform.mokaformserver.answer.dto.response.AnswerDetailResponse;
import com.mokaform.mokaformserver.answer.repository.AnswerRepository;
import com.mokaform.mokaformserver.answer.repository.EssayAnswerRepository;
import com.mokaform.mokaformserver.answer.repository.MultipleChoiceAnswerRepository;
import com.mokaform.mokaformserver.answer.repository.OXAnswerRepository;
import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.response.AnswerStatsResponse;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final EssayAnswerRepository essayAnswerRepository;
    private final MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;
    private final OXAnswerRepository oxAnswerRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;
    private final SurveyRepository surveyRepository;

    public AnswerService(AnswerRepository answerRepository,
                         EssayAnswerRepository essayAnswerRepository,
                         MultipleChoiceAnswerRepository multipleChoiceAnswerRepository,
                         OXAnswerRepository oxAnswerRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository,
                         SurveyRepository surveyRepository) {
        this.answerRepository = answerRepository;
        this.essayAnswerRepository = essayAnswerRepository;
        this.multipleChoiceAnswerRepository = multipleChoiceAnswerRepository;
        this.oxAnswerRepository = oxAnswerRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public void createAnswer(AnswerCreateRequest request, User user) {

        request.getEssayAnswers()
                .forEach(answer -> {
                            Answer savedAnswer = saveAnswer(Answer.builder()
                                    .user(user)
                                    .question(getQuestion(answer.getQuestionId()))
                                    .build());

                            saveEssayAnswer(
                                    EssayAnswer.builder()
                                            .answer(savedAnswer)
                                            .answerContent(answer.getAnswerContent())
                                            .build());
                        }

                );

        request.getMultipleChoiceAnswers()
                .forEach(answer -> {
                    Answer savedAnswer = saveAnswer(Answer.builder()
                            .user(user)
                            .question(getQuestion(answer.getQuestionId()))
                            .build());

                    MultipleChoiceQuestion multipleChoiceQuestion = getMultipleChoiceQuestion(answer.getMultiQuestionId());

                    saveMultipleChoiceAnswer(
                            MultipleChoiceAnswer.builder()
                                    .answer(savedAnswer)
                                    .multipleChoiceQuestion(multipleChoiceQuestion)
                                    .build()
                    );
                });

        request.getOxAnswers()
                .forEach(answer -> {
                            Answer savedAnswer = saveAnswer(Answer.builder()
                                    .user(user)
                                    .question(getQuestion(answer.getQuestionId()))
                                    .build());

                            saveOXAnswer(
                                    OXAnswer.builder()
                                            .answer(savedAnswer)
                                            .isYes(answer.getIsYes())
                                            .build()
                            );
                        }
                );

    }

    @Transactional(readOnly = true)
    public AnswerDetailResponse getAnswerDetail(String sharingKey, Long userId) {
        Survey survey = getSurveyBySharingKey(sharingKey);

        List<AnswerInfoMapping> answerInfos = answerRepository.findAnswerInfos(survey.getSurveyId(), userId);

        List<EssayAnswer> essayAnswers = new ArrayList<>();
        List<MultipleChoiceAnswer> multipleChoiceAnswers = new ArrayList<>();
        List<OXAnswer> oxAnswers = new ArrayList<>();
        answerInfos.forEach(answerInfo -> {
            getEssayAnswer(answerInfo.getAnswerId())
                    .ifPresent(essayAnswer -> essayAnswers.add(essayAnswer));
            getMultipleChoiceAnswer(answerInfo.getAnswerId())
                    .ifPresent(multipleChoiceAnswer -> multipleChoiceAnswers.add(multipleChoiceAnswer));
            getOxAnswer(answerInfo.getAnswerId())
                    .ifPresent(oxAnswer -> oxAnswers.add(oxAnswer));
        });

        return AnswerDetailResponse.builder()
                .surveyId(survey.getSurveyId())
                .surveyeeId(userId)
                .essayAnswers(essayAnswers)
                .multipleChoiceAnswers(multipleChoiceAnswers)
                .oxAnswers(oxAnswers)
                .build();
    }

    public AnswerStatsResponse getAnswerStats(Long surveyId) {
        List<EssayAnswerStatsMapping> essayAnswers = answerRepository.findEssayAnswers(surveyId);
        List<MultipleChoiceAnswerStatsMapping> multipleChoiceAnswers = answerRepository.findMultipleChoiceAnswers(surveyId);
        List<OXAnswerStatsMapping> oxAnswers = answerRepository.findOxAnswers(surveyId);

        Map<Long, List<String>> essayStats = new HashMap<>();
        Map<Long, Map<Long, Long>> multipleChoiceStats = new HashMap<>();
        Map<Long, Map<String, Long>> oxStats = new HashMap<>();
        essayAnswers.forEach(essayAnswer -> {
            if (essayStats.containsKey(essayAnswer.getQuestionId())) {
                List<String> value = essayStats.get(essayAnswer.getQuestionId());
                value.add(essayAnswer.getAnswerContent());
                essayStats.replace(essayAnswer.getQuestionId(), value);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(essayAnswer.getAnswerContent());
                essayStats.put(essayAnswer.getQuestionId(), list);
            }
        });
        multipleChoiceAnswers.forEach(multipleChoiceAnswer -> {
            if (multipleChoiceStats.containsKey(multipleChoiceAnswer.getQuestionId())) {
                Map<Long, Long> value = multipleChoiceStats.get(multipleChoiceAnswer.getQuestionId());
                if (value.containsKey(multipleChoiceAnswer.getMultiQuestionId())) {
                    value.replace(multipleChoiceAnswer.getMultiQuestionId(), value.get(multipleChoiceAnswer.getMultiQuestionId()) + 1L);
                } else {
                    value.put(multipleChoiceAnswer.getMultiQuestionId(), 1L);
                }
                multipleChoiceStats.replace(multipleChoiceAnswer.getQuestionId(), value);
            } else {
                Map<Long, Long> value = new HashMap<>();
                value.put(multipleChoiceAnswer.getMultiQuestionId(), 1L);
                multipleChoiceStats.put(multipleChoiceAnswer.getQuestionId(), value);
            }
        });
        oxAnswers.forEach(oxAnswer -> {
            if (oxStats.containsKey(oxAnswer.getQuestionId())) {
                Map<String, Long> value = oxStats.get(oxAnswer.getQuestionId());
                if (oxAnswer.getIsYes() == true) {
                    if (value.containsKey("yes")) {
                        value.replace("yes", value.get("yes") + 1L);
                    } else {
                        value.put("yes", 1L);
                    }
                } else {
                    if (value.containsKey("no")) {
                        value.replace("no", value.get("no") + 1L);
                    } else {
                        value.put("no", 1L);
                    }
                }
            } else {
                Map<String, Long> value = new HashMap<>();
                if (oxAnswer.getIsYes() == true) {
                    value.put("yes", 1L);
                } else {
                    value.put("no", 1L);
                }
                oxStats.put(oxAnswer.getQuestionId(), value);
            }
        });

        return AnswerStatsResponse.builder()
                .essayStats(essayStats)
                .multipleChoiceStats(multipleChoiceStats)
                .oxStats(oxStats)
                .build();
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new ApiException(CommonErrorCode.INVALID_PARAMETER));
    }

    private MultipleChoiceQuestion getMultipleChoiceQuestion(Long multiQuestionId) {
        return multiChoiceQuestionRepository.findById(multiQuestionId)
                .orElseThrow(() ->
                        new ApiException(CommonErrorCode.INVALID_PARAMETER));
    }

    private Answer saveAnswer(Answer answer) {
        Answer savedAnswer = answerRepository.save(answer);
        return savedAnswer;
    }

    private void saveEssayAnswer(EssayAnswer essayAnswer) {
        essayAnswerRepository.save(essayAnswer);
    }

    private void saveMultipleChoiceAnswer(MultipleChoiceAnswer multipleChoiceAnswer) {
        multipleChoiceAnswerRepository.save(multipleChoiceAnswer);
    }

    private void saveOXAnswer(OXAnswer oxAnswer) {
        oxAnswerRepository.save(oxAnswer);
    }

    private Optional<EssayAnswer> getEssayAnswer(Long answerId) {
        return essayAnswerRepository.findByAnswerId(answerId);
    }

    private Optional<MultipleChoiceAnswer> getMultipleChoiceAnswer(Long answerId) {
        return multipleChoiceAnswerRepository.findByAnswerId(answerId);
    }

    private Optional<OXAnswer> getOxAnswer(Long answerId) {
        return oxAnswerRepository.findByAnswerId(answerId);
    }

    private Survey getSurveyBySharingKey(String sharingKey) {
        return surveyRepository.findBySharingKey(sharingKey)
                .orElseThrow(() -> new ApiException(CommonErrorCode.INVALID_PARAMETER));
    }

}
