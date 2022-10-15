package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDetailsResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    private final SurveyService surveyService;

    private final UserRepository userRepository;

    public SurveyController(SurveyService surveyService, UserRepository userRepository) {
        this.surveyService = surveyService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSurvey(@RequestBody @Valid SurveyCreateRequest request,
                                                    @RequestParam Long userId) {

        // TODO: 로그인 구현 후에 삭제
        User user = getUser(userId);

        SurveyCreateResponse response = surveyService.createSurvey(request, user);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 설문 생성이 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSurveyDetails(@RequestParam(required = false) Long surveyId,
                                                        @RequestParam(required = false) String sharingKey) {
        SurveyDetailsResponse response = null;
        if ((Objects.nonNull(surveyId) && Objects.nonNull(sharingKey))
                || (Objects.isNull(surveyId) && Objects.isNull(sharingKey))) {
            throw new ApiException(CommonErrorCode.INVALID_REQUEST);
        } else if (Objects.nonNull(surveyId)) {
            response = surveyService.getSurveyDetailsById(surveyId);
        } else if (Objects.nonNull(sharingKey)) {
            response = surveyService.getSurveyDetailsBySharingKey(sharingKey);
        }

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 상세 조회가 성공하였습니다.")
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
