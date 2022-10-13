package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    private final SurveyService surveyCreateService;

    private final UserRepository userRepository;

    public SurveyController(SurveyService surveyCreateService, UserRepository userRepository) {
        this.surveyCreateService = surveyCreateService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSurvey(@RequestBody @Valid SurveyCreateRequest request,
                                                    @RequestParam Long userId) {

        // TODO: 로그인 구현 후에 삭제
        User user = getUser(userId);

        SurveyCreateResponse response = surveyCreateService.createSurvey(request, user);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 설문 생성이 성공하였습니다.")
                        .data(response)
                        .build());
    }

    // TODO: 로그인 구현 후에 삭제
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ApiException(CommonErrorCode.INVALID_PARAMETER));
    }

}
