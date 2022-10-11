package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.survey.domain.QuestionTmp;
import com.mokaform.mokaformserver.survey.dto.request.QuestionSaveRequest;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionTmpService {
    private final QuestionRepository questionRepository;

    public QuestionTmpService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void saveQuestionTmp(QuestionSaveRequest request) {
        QuestionTmp questionTmp = QuestionTmp.builder()
                .survey_id(request.getSurvey_id())
                .title(request.getTitle())
                .index(request.getIndex())
                .type(request.getType())
                .is_multi_answer(request.getIs_multi_answer())
                .build();

        questionRepository.save(questionTmp);
    }

}
