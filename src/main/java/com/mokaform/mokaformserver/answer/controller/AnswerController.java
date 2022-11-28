package com.mokaform.mokaformserver.answer.controller;

import com.mokaform.mokaformserver.answer.dto.request.AnswerCreateRequest;
import com.mokaform.mokaformserver.answer.dto.response.AnswerDetailResponse;
import com.mokaform.mokaformserver.answer.dto.response.stat.AnswerStatsResponse;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "답변", description = "설문 답변 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {
    private final AnswerService answerCreateService;

    public AnswerController(AnswerService answerCreateService) {
        this.answerCreateService = answerCreateService;
    }

    @Operation(summary = "설문 답변 등록", description = "설문의 답변을 등록하는 API입니다.")
    @PostMapping
    public ResponseEntity<ApiResponse> createAnswer(@RequestBody @Valid AnswerCreateRequest request,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        answerCreateService.createAnswer(request, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 답변 생성이 성공하였습니다.")
                        .build());

    }

    @Operation(summary = "내가 참여한 설문 상세 조회", description = "내가 참여한 설문 상세 조회하는 API입니다.")
    @GetMapping("/my/submitted-surveys/{sharingKey}")
    public ResponseEntity<ApiResponse<AnswerDetailResponse>> getSubmittedSurveyDetail(@PathVariable(value = "sharingKey") String sharingKey,
                                                                                      @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        AnswerDetailResponse response = answerCreateService.getAnswerDetail(sharingKey, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("내가 참여한 설문 상세 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "내가 생성한 설문의 통계 결과 조회", description = "내가 생성한 설문의 통계 결과 조회하는 API입니다.")
    @GetMapping("/my/surveys/{surveyId}/stats")
    public ResponseEntity<ApiResponse<AnswerStatsResponse>> getAnswerStats(@PathVariable(value = "surveyId") Long surveyId,
                                                                           @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        AnswerStatsResponse response = answerCreateService.getAnswerStats(surveyId, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 통계 결과 조회 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }


}
