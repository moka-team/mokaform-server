package com.mokaform.mokaformserver.answer.service;

import com.mokaform.mokaformserver.answer.domain.Answer;
import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import com.mokaform.mokaformserver.answer.dto.mapping.AnswerInfoMapping;
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
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final EssayAnswerRepository essayAnswerRepository;
    private final MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;
    private final OXAnswerRepository oxAnswerRepository;
    private final QuestionRepository questionRepository;
    private final MultiChoiceQuestionRepository multiChoiceQuestionRepository;

    public AnswerService(AnswerRepository answerRepository, EssayAnswerRepository essayAnswerRepository,
                         MultipleChoiceAnswerRepository multipleChoiceAnswerRepository,
                         OXAnswerRepository oxAnswerRepository,
                         QuestionRepository questionRepository,
                         MultiChoiceQuestionRepository multiChoiceQuestionRepository) {
        this.answerRepository = answerRepository;
        this.essayAnswerRepository = essayAnswerRepository;
        this.multipleChoiceAnswerRepository = multipleChoiceAnswerRepository;
        this.oxAnswerRepository = oxAnswerRepository;
        this.questionRepository = questionRepository;
        this.multiChoiceQuestionRepository = multiChoiceQuestionRepository;
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
    public AnswerDetailResponse getAnswerDetail(Long surveyId, Long userId) {
        List<AnswerInfoMapping> answerInfos = answerRepository.findAnswerInfos(surveyId, userId);

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
                .surveyId(surveyId)
                .surveyeeId(userId)
                .essayAnswers(essayAnswers)
                .multipleChoiceAnswers(multipleChoiceAnswers)
                .oxAnswers(oxAnswers)
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

}
