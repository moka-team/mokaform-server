package com.mokaform.mokaformserver.user.controller;

import com.mokaform.mokaformserver.answer.dto.response.AnswerDetailResponse;
import com.mokaform.mokaformserver.answer.dto.response.stat.AnswerStatsResponse;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.jwt.JwtAuthenticationToken;
import com.mokaform.mokaformserver.common.jwt.JwtService;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.response.SubmittedSurveyInfoResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.dto.request.LocalLoginRequest;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.dto.request.TokenReissueRequest;
import com.mokaform.mokaformserver.user.dto.response.DuplicateValidationResponse;
import com.mokaform.mokaformserver.user.dto.response.LocalLoginResponse;
import com.mokaform.mokaformserver.user.dto.response.UserGetResponse;
import com.mokaform.mokaformserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "유저", description = "유저 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final SurveyService surveyService;
    private final AnswerService answerService;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          SurveyService surveyService,
                          AnswerService answerService,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.surveyService = surveyService;
        this.answerService = answerService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "회원가입", description = "회원가입 API입니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignupRequest request) {
        userService.createUser(request);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 유저 생성이 성공하였습니다.")
                        .build());
    }

    @Operation(summary = "내가 작성한 설문 다건 조회", description = "내가 작성한 설문 다건 조회하는 API입니다.")
    @GetMapping("/my/surveys")
    public ResponseEntity<ApiResponse<SurveyInfoResponse>> getSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                          @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        PageResponse<SurveyInfoResponse> response = surveyService.getSurveyInfos(pageable, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("내가 작성한 설문 다건 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "내가 참여한 설문 다건 조회", description = "내가 참여한 설문 다건 조회하는 API입니다.")
    @GetMapping("/my/submitted-surveys")
    public ResponseEntity<ApiResponse<SubmittedSurveyInfoResponse>> getSubmittedSurveyInfos(@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable,
                                                                                            @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        PageResponse<SubmittedSurveyInfoResponse> response = surveyService.getSubmittedSurveyInfos(pageable, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("내가 참여한 설문 다건 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "내가 참여한 설문 상세 조회", description = "내가 참여한 설문 상세 조회하는 API입니다.")
    @GetMapping("/my/submitted-surveys/{sharingKey}")
    public ResponseEntity<ApiResponse<AnswerDetailResponse>> getSubmittedSurveyDetail(@PathVariable(value = "sharingKey") String sharingKey,
                                                                                      @Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        AnswerDetailResponse response = answerService.getAnswerDetail(sharingKey, authentication.email);

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
        AnswerStatsResponse response = answerService.getAnswerStats(surveyId, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("설문 통계 결과 조회 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 체크하는 API입니다.")
    @GetMapping("/check-email-duplication")
    public ResponseEntity<ApiResponse<DuplicateValidationResponse>> checkEmailDuplication(@RequestParam(value = "email") String email) {
        DuplicateValidationResponse response = userService.checkEmailDuplication(email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("이메일 중복 확인 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 체크하는 API입니다.")
    @GetMapping("/check-nickname-duplication")
    public ResponseEntity<ApiResponse<DuplicateValidationResponse>> checkNicknameDuplication(@RequestParam(value = "nickname") String nickname) {
        DuplicateValidationResponse response = userService.checkNicknameDuplication(nickname);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("닉네임 중복 확인 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "로그인", description = "로그인하는 API입니다.")
    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<LocalLoginResponse>> login(@RequestBody @Valid LocalLoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        String refreshToken = (String) resultToken.getDetails();
        LocalLoginResponse response = new LocalLoginResponse(authentication.accessToken, refreshToken, authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("로그인 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "나의 정보 조회", description = "나의 정보 조회하는 API입니다.")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUser(@Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        UserGetResponse response = userService.getUserInfo(authentication.email);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("나의 정보 조회가 성공하였습니다.")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @Operation(summary = "로그아웃", description = "로그아웃하는 API입니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@Parameter(hidden = true) @AuthenticationPrincipal JwtAuthentication authentication) {
        jwtService.logout(authentication.accessToken);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("로그아웃을 성공하였습니다.")
                        .build());
    }

    @Operation(summary = "토큰 재발급", description = "access token을 재발급하는 API입니다.")
    @PostMapping("/token/reissue")
    public ResponseEntity<ApiResponse> reissueAccessToken(@RequestBody @Valid TokenReissueRequest request) {
        String newAccessToken = jwtService.reissueAccessToken(request.getAccessToken(), request.getRefreshToken());
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("토큰이 재발급되었습니다.")
                        .data(newAccessToken)
                        .build());
    }
}
