package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.QuestionTmp;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.request.MCQuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.QuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.SurveySaveRequest;
import com.mokaform.mokaformserver.survey.repository.MCQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionTmpRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateSurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionTmpRepository questionTmpRepository;
    private final MCQuestionRepository mcQuestionRepository;

    public CreateSurveyService(SurveyRepository surveyRepository, QuestionTmpRepository questionTmpRepository, MCQuestionRepository mcQuestionRepository) {
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

        QuestionTmp questionTmp = QuestionTmp.builder()
                .survey(request.getSurvey())
                .title(request.getTitle())
                .index(request.getIndex())
                .type(request.getType())
                .isMultiAnswer(request.getIsMultiAnswer())
                .build();

        QuestionTmp saveQuestionTmp = questionTmpRepository.save(questionTmp);

        return saveQuestionTmp.getQuestionId();
    }

    public void saveMCQuestion(MCQuestionSaveRequest request) {
        QuestionTmp questionTmp = questionTmpRepository.findById(request.getQuestionId())
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
