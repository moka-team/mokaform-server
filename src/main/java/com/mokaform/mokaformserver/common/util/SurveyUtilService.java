package com.mokaform.mokaformserver.common.util;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.SurveyErrorCode;
import com.mokaform.mokaformserver.survey.domain.Survey;
import com.mokaform.mokaformserver.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyUtilService {

    private final SurveyRepository surveyRepository;

    public SurveyUtilService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Transactional(readOnly = true)
    public Survey getSurvey(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApiException(SurveyErrorCode.SURVEY_NOT_FOUND));
    }
}
