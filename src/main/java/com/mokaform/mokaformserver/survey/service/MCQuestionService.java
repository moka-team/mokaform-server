package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.dto.request.MCQuestionSaveRequest;
import com.mokaform.mokaformserver.survey.repository.MCQuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class MCQuestionService {
    private final MCQuestionRepository mcQuestionRepository;

    public MCQuestionService(MCQuestionRepository mcQuestionRepository) {
        this.mcQuestionRepository = mcQuestionRepository;
    }

    public void saveMCQuestion(MCQuestionSaveRequest request) {
        MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestion.builder()
                .question_id(request.getQuestion_id())
                .multi_question_content(request.getMulti_question_content())
                .multi_question_index(request.getMulti_question_index())
                .multi_question_type(request.getMulti_question_type())
                .build();

        mcQuestionRepository.save(multipleChoiceQuestion);
    }
}
