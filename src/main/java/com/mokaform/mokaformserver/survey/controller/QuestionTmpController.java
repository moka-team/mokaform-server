package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.survey.dto.request.QuestionSaveRequest;
import com.mokaform.mokaformserver.survey.service.CreateSurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/detail-survey")
public class QuestionTmpController {
    private final CreateSurveyService createSurveyService;

    public QuestionTmpController(CreateSurveyService createSurveyService) {
        this.createSurveyService = createSurveyService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveQuestionTmp(@RequestBody @Valid QuestionSaveRequest request) {
        createSurveyService.saveQuestionTmp(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 질문 생성이 성공하였습니다.")
                        .build());

    }
}
