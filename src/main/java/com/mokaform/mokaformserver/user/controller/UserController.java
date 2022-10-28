package com.mokaform.mokaformserver.user.controller;

import com.mokaform.mokaformserver.answer.dto.response.AnswerDetailResponse;
import com.mokaform.mokaformserver.answer.dto.response.stat.AnswerStatsResponse;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.jwt.JwtAuthenticationToken;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.response.SubmittedSurveyInfoResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.dto.request.LocalLoginRequest;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.dto.response.DuplicateValidationResponse;
import com.mokaform.mokaformserver.user.dto.response.LocalLoginResponse;
import com.mokaform.mokaformserver.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final SurveyService surveyService;
    private final AnswerService answerService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          SurveyService surveyService,
                          AnswerService answerService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.surveyService = surveyService;
        this.answerService = answerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignupRequest request) {
        userService.createUser(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 유저 생성이 성공하였습니다.")
                        .build());
    }

    @GetMapping("/my/surveys")
    public ResponseEntity<ApiResponse> getSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                      @AuthenticationPrincipal JwtAuthentication authentication) {
        PageResponse<SurveyInfoResponse> response = surveyService.getSurveyInfos(pageable, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 작성한 설문 다건 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/my/submitted-surveys")
    public ResponseEntity<ApiResponse> getSubmittedSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                               @AuthenticationPrincipal JwtAuthentication authentication) {
        PageResponse<SubmittedSurveyInfoResponse> response = surveyService.getSubmittedSurveyInfos(pageable, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 참여한 설문 다건 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/my/submitted-surveys/{sharingKey}")
    public ResponseEntity<ApiResponse> getSubmittedSurveyDetail(@PathVariable(value = "sharingKey") String sharingKey,
                                                                @AuthenticationPrincipal JwtAuthentication authentication) {
        AnswerDetailResponse response = answerService.getAnswerDetail(sharingKey, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("내가 참여한 설문 상세 조회가 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/my/surveys/{surveyId}/stats")
    public ResponseEntity<ApiResponse> getAnswerStats(@PathVariable(value = "surveyId") Long surveyId,
                                                      @AuthenticationPrincipal JwtAuthentication authentication) {
        AnswerStatsResponse response = answerService.getAnswerStats(surveyId, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("설문 통계 결과 조회 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/check-email-duplication")
    public ResponseEntity<ApiResponse> checkEmailDuplication(@RequestParam(value = "email") String email) {
        DuplicateValidationResponse response = userService.checkEmailDuplication(email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("이메일 중복 확인 성공하였습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/check-nickname-duplication")
    public ResponseEntity<ApiResponse> checkNicknameDuplication(@RequestParam(value = "nickname") String nickname) {
        DuplicateValidationResponse response = userService.checkNicknameDuplication(nickname);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("닉네임 중복 확인 성공하였습니다.")
                        .data(response)
                        .build());
    }

    /**
     * 사용자 로그인
     */
    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LocalLoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        String refreshToken = (String) resultToken.getDetails();
        LocalLoginResponse response = new LocalLoginResponse(authentication.accessToken, refreshToken, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("로그인 성공하였습니다.")
                        .data(response)
                        .build());
    }

}
