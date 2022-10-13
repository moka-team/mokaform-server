package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.request.MCQuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.QuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.SurveySaveRequest;
import com.mokaform.mokaformserver.survey.repository.MultiChoiceQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionTmpRepository;
    private final MultiChoiceQuestionRepository mcQuestionRepository;

    public SurveyService(SurveyRepository surveyRepository, QuestionRepository questionTmpRepository, MultiChoiceQuestionRepository mcQuestionRepository) {
        this.surveyRepository = surveyRepository;
        this.questionTmpRepository = questionTmpRepository;
        this.mcQuestionRepository = mcQuestionRepository;
    }

    public Long saveSurvey(SurveySaveRequest request) {
        Survey survey = Survey.builder()
                .surveyor_id(request.getSurveyorId())
                .title(request.getTitle())
                .is_anonymous(request.getIsAnonymous())
                .is_public(request.getIsPublic())
                .start_date(request.getStartDate())
                .end_date(request.getEndDate())
                .build();

        Survey savedSurvey = surveyRepository.save(survey);

        return savedSurvey.getSurveyId();
    }

    public Long saveQuestionTmp(QuestionSaveRequest request) {
//        Survey survey = surveyRepository.findById(request.getSurveyId())
//                .orElseThrow(() -> new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        Question questionTmp = Question.builder()
                .survey(request.getSurvey())
                .title(request.getTitle())
                .index(request.getIndex())
                .type(request.getType())
                .isMultiAnswer(request.getIsMultiAnswer())
                .build();

        Question saveQuestionTmp = questionTmpRepository.save(questionTmp);

        return saveQuestionTmp.getQuestionId();
    }

    public void saveMCQuestion(MCQuestionSaveRequest request) {
        Question questionTmp = questionTmpRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestion.builder()
                .questionTmp(questionTmp)
                .multiQuestionContent(request.getMultiQuestionContent())
                .multiQuestionIndex(request.getMultiQuestionIndex())
                .multiQuestionType(request.getMultiQuestionType())
                .build();

        mcQuestionRepository.save(multipleChoiceQuestion);
    }
}
