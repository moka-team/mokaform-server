package com.mokaform.mokaformserver.user.controller;

import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.common.response.PageResponse;
import com.mokaform.mokaformserver.survey.dto.response.SurveyInfoResponse;
import com.mokaform.mokaformserver.survey.service.SurveyService;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
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

    public UserController(UserService userService, SurveyService surveyService) {
        this.userService = userService;
        this.surveyService = surveyService;
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

}
