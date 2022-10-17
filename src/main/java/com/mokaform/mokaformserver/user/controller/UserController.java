package com.mokaform.mokaformserver.user.controller;

import com.mokaform.mokaformserver.answer.dto.response.AnswerDetailResponse;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.response.AnswerStatsResponse;
import com.mokaform.mokaformserver.survey.dto.response.SubmittedSurveyInfoResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.dto.request.LoginRequest;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.dto.response.LoginResponse;
import com.mokaform.mokaformserver.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final SurveyService surveyService;
    private final AnswerService answerService;

    public UserController(UserService userService,
                          SurveyService surveyService,
                          AnswerService answerService) {
        this.userService = userService;
        this.surveyService = surveyService;
        this.answerService = answerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignupRequest request) {
        userService.createUser(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 유저 생성이 성공하였습니다.")
                        .build());
    }

    // TODO: userId는 로그인 구현 후에 수정
    @GetMapping("/my/surveys")
    public ResponseEntity<ApiResponse> getSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                      @RequestParam Long userId) {
        PageResponse<SurveyInfoResponse> response = surveyService.getSurveyInfos(pageable, userId);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 작성한 설문 다건 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    // TODO: userId는 로그인 구현 후에 수정
    @GetMapping("/my/submitted-surveys")
    public ResponseEntity<ApiResponse> getSubmittedSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                               @RequestParam Long userId) {
        PageResponse<SubmittedSurveyInfoResponse> response = surveyService.getSubmittedSurveyInfos(pageable, userId);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 참여한 설문 다건 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    // TODO: userId는 로그인 구현 후에 수정
    @GetMapping("/my/submitted-surveys/{sharingKey}")
    public ResponseEntity<ApiResponse> getSubmittedSurveyDetail(@PathVariable(value = "sharingKey") String sharingKey,
                                                                @RequestParam Long userId) {
        AnswerDetailResponse response = answerService.getAnswerDetail(sharingKey, userId);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 참여한 설문 상세 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    // TODO: 로그인 구현 후에 수정
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.getUser(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("로그읜 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/my/surveys/{surveyId}/stats")
    public ResponseEntity<ApiResponse> getAnswerStats(@PathVariable(value = "surveyId") Long surveyId) {
        AnswerStatsResponse response = answerService.getAnswerStats(surveyId);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 통계 결과 조회 성공하였습니다.")
                        .data(response)
                        .build());
    }

}
