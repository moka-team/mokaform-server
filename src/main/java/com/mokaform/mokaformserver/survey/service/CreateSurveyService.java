package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.QuestionTmp;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.request.MCQuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.QuestionSaveRequest;
import com.mokaform.mokaformserver.survey.dto.request.SurveySaveRequest;
import com.mokaform.mokaformserver.survey.repository.MCQuestionRepository;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateSurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final MCQuestionRepository mcQuestionRepository;

    Survey survey;
    QuestionTmp questionTmp;

    public CreateSurveyService(SurveyRepository surveyRepository, QuestionRepository questionRepository, MCQuestionRepository mcQuestionRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.mcQuestionRepository = mcQuestionRepository;
    }

    public void saveSurvey(SurveySaveRequest request) {
        survey = Survey.builder()
                .surveyor_id(request.getSurveyor_id())
                .title(request.getTitle())
                .is_anonymous(request.getIs_anonymous())
                .is_public(request.getIs_public())
                .is_deleted(request.getIs_deleted())
                .sharing_key(request.getSharing_key())
                .build();

        surveyRepository.save(survey);
    }

    public void saveQuestionTmp(QuestionSaveRequest request) {
        questionTmp = QuestionTmp.builder()
                .survey(survey)
                .title(request.getTitle())
                .index(request.getIndex())
                .type(request.getType())
                .is_multi_answer(request.getIs_multi_answer())
                .build();

        questionRepository.save(questionTmp);
    }

    public void saveMCQuestion(MCQuestionSaveRequest request) {
        MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestion.builder()
                .questionTmp(questionTmp)
                .multi_question_content(request.getMulti_question_content())
                .multi_question_index(request.getMulti_question_index())
                .multi_question_type(request.getMulti_question_type())
                .build();

        mcQuestionRepository.save(multipleChoiceQuestion);
    }
}
