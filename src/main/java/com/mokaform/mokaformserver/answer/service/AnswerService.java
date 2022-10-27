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
import com.mokaform.mokaformserver.answer.dto.response.stat.*;
import com.mokaform.mokaformserver.answer.repository.AnswerRepository;
import com.mokaform.mokaformserver.answer.repository.EssayAnswerRepository;
import com.mokaform.mokaformserver.answer.repository.MultipleChoiceAnswerRepository;
import com.mokaform.mokaformserver.answer.repository.OXAnswerRepository;
import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.util.UserUtilService;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final EssayAnswerRepository essayAnswerRepository;
    private final MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;
    private final OXAnswerRepository oxAnswerRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;
    private final SurveyRepository surveyRepository;

    private final UserUtilService userUtilService;

    public AnswerService(AnswerRepository answerRepository,
                         EssayAnswerRepository essayAnswerRepository,
                         MultipleChoiceAnswerRepository multipleChoiceAnswerRepository,
                         OXAnswerRepository oxAnswerRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository,
                         SurveyRepository surveyRepository,
                         UserUtilService userUtilService) {
        this.answerRepository = answerRepository;
        this.essayAnswerRepository = essayAnswerRepository;
        this.multipleChoiceAnswerRepository = multipleChoiceAnswerRepository;
        this.oxAnswerRepository = oxAnswerRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
        this.surveyRepository = surveyRepository;
        this.userUtilService = userUtilService;
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
    public AnswerDetailResponse getAnswerDetail(String sharingKey, String userEmail) {
        User user = userUtilService.getUser(userEmail);
        Survey survey = getSurveyBySharingKey(sharingKey);

        List<AnswerInfoMapping> answerInfos = answerRepository.findAnswerInfos(survey.getSurveyId(), user.getId());

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
                .surveyeeId(user.getId())
                .essayAnswers(essayAnswers)
                .multipleChoiceAnswers(multipleChoiceAnswers)
                .oxAnswers(oxAnswers)
                .build();
    }

    public AnswerStatsResponse getAnswerStats(Long surveyId) {
        List<EssayAnswerStatsMapping> essayAnswers = answerRepository.findEssayAnswers(surveyId);
        List<MultipleChoiceAnswerStatsMapping> multipleChoiceAnswers = answerRepository.findMultipleChoiceAnswers(surveyId);
        List<OXAnswerStatsMapping> oxAnswers = answerRepository.findOxAnswers(surveyId);

        /**
         * 질문(question)별로 Map으로 묶어줌
         */
        Map<Long, List<EssayAnswerStatsMapping>> essayStatMap = new HashMap<>();
        essayAnswers.forEach(essayAnswer -> {
            if (essayStatMap.containsKey(essayAnswer.getQuestionId())) {
                List<EssayAnswerStatsMapping> value = essayStatMap.get(essayAnswer.getQuestionId());
                value.add(essayAnswer);
                essayStatMap.replace(essayAnswer.getQuestionId(), value);
            } else {
                ArrayList<EssayAnswerStatsMapping> list = new ArrayList<>();
                list.add(essayAnswer);
                essayStatMap.put(essayAnswer.getQuestionId(), list);
            }
        });

        Map<Long, List<MultipleChoiceAnswerStatsMapping>> multipleChoiceStatMap = new HashMap<>();
        multipleChoiceAnswers.forEach(multipleChoiceAnswer -> {
            if (multipleChoiceStatMap.containsKey(multipleChoiceAnswer.getQuestionId())) {
                List<MultipleChoiceAnswerStatsMapping> value = multipleChoiceStatMap.get(multipleChoiceAnswer.getQuestionId());
                value.add(multipleChoiceAnswer);
                multipleChoiceStatMap.replace(multipleChoiceAnswer.getQuestionId(), value);
            } else {
                ArrayList<MultipleChoiceAnswerStatsMapping> list = new ArrayList<>();
                list.add(multipleChoiceAnswer);
                multipleChoiceStatMap.put(multipleChoiceAnswer.getQuestionId(), list);
            }
        });


        /**
         * Response 포맷에 맞게 Map -> List로 변형
         */
        List<EssayStat> essayStats = essayStatMap.entrySet().stream()
                .map(entry -> {
                    EssayAnswerStatsMapping questionInfo = entry.getValue().get(0);
                    return EssayStat.builder()
                            .questionIndex(questionInfo.getQuestionIndex())
                            .title(questionInfo.getTitle())
                            .answerContents(entry.getValue().stream()
                                    .map(EssayAnswerStatsMapping::getAnswerContent)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        List<MultipleChoiceStat> multipleChoiceStats = multipleChoiceStatMap.entrySet().stream()
                .map(entry -> {
                    MultipleChoiceAnswerStatsMapping questionInfo = entry.getValue().get(0);
                    return MultipleChoiceStat.builder()
                            .questionIndex(questionInfo.getQuestionIndex())
                            .title(questionInfo.getTitle())
                            .results(entry.getValue().stream()
                                    .map(MultipleChoiceResult::new)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        List<OXStat> oxStats = oxAnswers.stream().map(OXStat::new).collect(Collectors.toList());

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
