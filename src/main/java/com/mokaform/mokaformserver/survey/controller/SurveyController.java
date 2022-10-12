package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.survey.dto.request.SurveySaveRequest;
import com.mokaform.mokaformserver.survey.service.CreateSurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    private final CreateSurveyService createSurveyService;


    public SurveyController(CreateSurveyService createSurveyService) {
        this.createSurveyService = createSurveyService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> saveSurvey(@RequestBody @Valid SurveySaveRequest request) {
        createSurveyService.saveSurvey(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 설문 생성이 성공하였습니다.")
                        .build());
    }
}
