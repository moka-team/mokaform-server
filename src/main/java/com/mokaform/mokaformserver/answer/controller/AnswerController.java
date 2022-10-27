package com.mokaform.mokaformserver.answer.controller;

import com.mokaform.mokaformserver.answer.dto.request.AnswerCreateRequest;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {
    private final AnswerService answerCreateService;

    public AnswerController(AnswerService answerCreateService) {
        this.answerCreateService = answerCreateService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAnswer(@RequestBody @Valid AnswerCreateRequest request,
                                                    @AuthenticationPrincipal JwtAuthentication authentication) {
        answerCreateService.createAnswer(request, authentication.email);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 답변 생성이 성공하였습니다.")
                        .build());

    }

}
