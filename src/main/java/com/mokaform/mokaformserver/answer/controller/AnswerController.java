package com.mokaform.mokaformserver.answer.controller;

import com.mokaform.mokaformserver.answer.dto.request.AnswerCreateRequest;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import com.mokaform.mokaformserver.survey.repository.QuestionRepository;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {
    private final AnswerService answerCreateService;
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    public AnswerController(AnswerService answerCreateService, UserRepository userRepository,
                            QuestionRepository questionRepository) {
        this.answerCreateService = answerCreateService;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAnswer(@RequestBody @Valid AnswerCreateRequest request,
                                                    @RequestParam Long userId) {
        // TODO: 로그인 구현 후에 삭제
        User user = getUser(userId);

        answerCreateService.createAnswer(request, user);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("새로운 답변 생성이 성공하였습니다.")
                        .build());

    }

    // TODO: 로그인 구현 후에 삭제
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ApiException(CommonErrorCode.INVALID_PARAMETER));
    }
}