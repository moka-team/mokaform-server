package com.mokaform.mokaformserver.survey.service;

import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.dto.request.SurveySaveRequest;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public  SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public void saveSurvey(SurveySaveRequest request) {
        Survey survey = Survey.builder()
                .surveyor_id(request.getSurveyor_id())
                .title(request.getTitle())
                .sharing_key(request.getSharing_key())
                .build();

        surveyRepository.save(survey);
    }
}
