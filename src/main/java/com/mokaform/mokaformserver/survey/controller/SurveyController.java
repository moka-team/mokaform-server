package com.mokaform.mokaformserver.survey.controller;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.request.SurveyCreateRequest;
import com.mokaform.mokaformserver.survey.dto.request.SurveyUpdateRequest;
import com.mokaform.mokaformserver.survey.dto.response.SurveyCreateResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDeleteResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyDetailsResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "설문", description = "설문 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Operation(summary = "설문 생성", description = "설문을 생성하는 API입니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<SurveyCreateResponse>> createSurvey(@RequestBody @Valid SurveyCreateRequest request,
                                                                          @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyCreateResponse response = surveyService.createSurvey(request, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("새로운 설문 생성이 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "설문 상세 조회", description = "설문을 상세 조회하는 API입니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<SurveyDetailsResponse>> getSurveyDetails(@RequestParam(required = false) Long surveyId,
                                                                               @RequestParam(required = false) String sharingKey,
                                                                               @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyDetailsResponse response = null;
        if ((Objects.nonNull(surveyId) && Objects.nonNull(sharingKey))
                || (Objects.isNull(surveyId) && Objects.isNull(sharingKey))) {
            throw new ApiException(CommonErrorCode.INVALID_REQUEST);
        } else if (Objects.nonNull(surveyId)) {
            response = surveyService.getSurveyDetailsById(surveyId, authentication.email);
        } else if (Objects.nonNull(sharingKey)) {
            response = surveyService.getSurveyDetailsBySharingKey(sharingKey, authentication.email);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 상세 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "설문 다건 조회", description = "설문을 다건 조회하는 API입니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<SurveyInfoResponse>> getSurveyInfos(@Parameter(description = "sort: {createdAt, surveyeeCount}, {asc, desc} 가능 => 예시: \"createdAt,desc\"")
                                                                          @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PageResponse<SurveyInfoResponse> response = surveyService.getSurveyInfos(pageable, null);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 다건 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "카테고리별 추천 설문 다건 조회", description = "카테고리별 추천 설문을 다건 조회하는 API입니다.")
    @GetMapping("/recommended-list")
    public ResponseEntity<ApiResponse<SurveyInfoResponse>> getRecommendedSurveyInfos(@Parameter(description = "sort: {createdAt, surveyeeCount}, {asc, desc} 가능 => 예시: \"createdAt,desc\"")
                                                                                     @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                                     @Parameter(description = "DAILY_LIFE, IT, HOBBY, LEARNING, PSYCHOLOGY, SOCIAL_POLITICS, PREFERENCE_RESEARCH, PET")
                                                                                     @RequestParam(value = "category") String category) {
        PageResponse<SurveyInfoResponse> response = surveyService.getRecommendedSurveyInfos(pageable, category);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("카테고리별 추천 설문 다건 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "설문 삭제", description = "설문을 삭제하는 API입니다.")
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<SurveyDeleteResponse>> removeSurvey(@PathVariable(value = "surveyId") Long surveyId,
                                                                          @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        SurveyDeleteResponse response = surveyService.deleteSurvey(surveyId, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 삭제가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "설문 수정", description = "설문을 수정하는 API입니다. (설문 시작일 전에만 수정 가능)")
    @PatchMapping("/{surveyId}")
    public ResponseEntity<ApiResponse> updateSurvey(@PathVariable(value = "surveyId") Long surveyId,
                                                    @RequestBody @Valid SurveyUpdateRequest request,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        surveyService.updateSurveyInfoAndQuestions(surveyId, authentication.email, request);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 수정이 성공하였습니다.")
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

}
