package com.mokaform.mokaformserver.answer.controller;

import com.mokaform.mokaformserver.answer.dto.request.AnswerCreateRequest;
import com.mokaform.mokaformserver.answer.service.AnswerService;
import com.mokaform.mokaformserver.common.jwt.JwtAuthentication;
import com.mokaform.mokaformserver.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @Operation(summary = "주관식 감정 분석", description = "주관식 답변의 감정을 분석하는 API입니다.")
    @GetMapping("/conversation/analysis")
    public ResponseEntity<ApiResponse> sentimentAnalysis(@RequestBody String str){

        String token = "4a395f44b5577975b95f98095d59fd5c";
        String URL = "https://42d1adfb-5b9c-4cb1-8351-670c4a4c3b25.api.kr-central-1.kakaoi.io/ai/conversation/f54447ad459c4079880862d1b333d9e6";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("msg",str);

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key",token);
        headers.add("Content-Type","application/json");

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .message("감정 분석이 완료되었습니다.")
                        .data(restTemplate.exchange(URL, HttpMethod.POST,entity,String.class))
                        .build());
    }


}
