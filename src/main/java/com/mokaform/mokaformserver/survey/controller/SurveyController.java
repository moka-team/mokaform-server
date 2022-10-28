package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDeleteResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDetailsResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSurvey(@RequestBody @Valid SurveyCreateRequest request,
                                                    @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyCreateResponse response = surveyService.createSurvey(request, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 설문 생성이 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSurveyDetails(@RequestParam(required = false) Long surveyId,
                                                        @RequestParam(required = false) String sharingKey,
                                                        @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyDetailsResponse response = null;
        if ((Objects.nonNull(surveyId) && Objects.nonNull(sharingKey))
                || (Objects.isNull(surveyId) && Objects.isNull(sharingKey))) {
            throw new ApiException(CommonErrorCode.INVALID_REQUEST);
        } else if (Objects.nonNull(surveyId)) {
            response = surveyService.getSurveyDetailsById(surveyId, authentication.email);
        } else if (Objects.nonNull(sharingKey)) {
            response = surveyService.getSurveyDetailsBySharingKey(sharingKey, authentication.email);
        }

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 상세 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PageResponse<SurveyInfoResponse> response = surveyService.getSurveyInfos(pageable, null);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 다건 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{surveyId}")
    public ResponseEntity<ApiResponse> removeSurvey(@PathVariable(value = "surveyId") Long surveyId,
                                                    @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyDeleteResponse response = surveyService.deleteSurvey(surveyId, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 삭제가 성공하였습니다.")
                        .data(response)
                        .build());
    }

}
